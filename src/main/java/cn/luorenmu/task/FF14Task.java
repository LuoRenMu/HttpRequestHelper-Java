package cn.luorenmu.task;

import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.common.cache.FileCache;
import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.entiy.TaskCache;
import cn.luorenmu.exception.CurrentTaskCannotContinueException;
import cn.luorenmu.request.ff14.FF14AccountRequest;
import cn.luorenmu.request.ff14.FF14ForumRequest;
import cn.luorenmu.request.ff14.entiy.*;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LoMu
 * Date 2024.01.28 20:55
 */
@Slf4j
public class FF14Task {
    private final static FF14ForumRequest ff14ForumRequest = new FF14ForumRequest();
    @CurrentSetting
    private static Setting setting;

    public boolean forumTask() {
        Setting.Account account = setting.getAccounts().get(Integer.parseInt(Thread.currentThread().getName()));
        RunStorage.accountThreadLocal.set(account);

        if (RunStorage.accountThreadLocal.get().getFf14() == null || RunStorage.accountThreadLocal.get().getFf14().getCookie().isBlank()) {
            throw new CurrentTaskCannotContinueException("FF14: setting is blank or setting is null");
        }

        FF14ForumRequest ff14ForumRequest = new FF14ForumRequest();
        FF14AccountRequest ff14AccountRequest = new FF14AccountRequest();


        FF14IsLoginResponse login = ff14AccountRequest.isLogin();
        if (login.isSuccess()) {
            String sealRewardStr = sealTask();
            int signInDay = signInTask();
            ff14ForumRequest.comment();
            ff14ForumRequest.createDynamic();


            FF14IsLoginResponse.FF14IsLoginData loginData = login.getData().get(0);

            Notifications.sendAllNotification("FF14签到已完成", String.format("当前角色: %s:%s ,已签到: %s  盖章: %s"
                    , loginData.getGroupName(), loginData.getCharacterName(), signInDay, sealRewardStr));
            TaskCache taskCache = FileCache.readCache(Thread.currentThread().getName());
            taskCache.setFf14(LocalDateTime.now());
            FileCache.writeCache(Thread.currentThread().getName(), taskCache);
            return true;
        }
        return false;
    }

    public void likeTask(int num) {
        int execute = 5 - num;
        if (execute != 0) {
            for (int i = 0; i < execute * 2 + 1; i++) {
                FF14Response like = ff14ForumRequest.like();
                like.isSuccess();
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String sealTask() {
        FF14MyTaskInfoResponse ff14MyTaskInfoResponse = ff14ForumRequest.myTaskInfo();
        if (ff14MyTaskInfoResponse.isSuccess()) {
            FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData sealDayTask = ff14MyTaskInfoResponse.getData().getDayTask();
            likeTask(sealDayTask.getLikeNum());

            if (!sealDayTask.signSealComplete()) {
                if (sealDayTask.getSignStatus() != 1) {
                    FF14SignInResponse ff14SignInResponse = ff14ForumRequest.signIn();
                    ff14SignInResponse.isSuccess();
                }
                ff14ForumRequest.doSeal("1");
            }

            if (!sealDayTask.likeSealComplete()) {
                ff14ForumRequest.doSeal("2");
            }

            if (!sealDayTask.commentSealComplete()) {
                if (sealDayTask.getCommentSeal() > 0) {
                    ff14ForumRequest.doSeal("3");
                } else {
                    FF14Response comment = ff14ForumRequest.comment();
                    if (comment.isSuccess()) {
                        ff14ForumRequest.doSeal("3");
                    }
                }
            }
            FF14MyTaskInfoResponse taskInfo = ff14ForumRequest.myTaskInfo();
            if (taskInfo.isSuccess()) {
                FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoOnceTaskData onceTask = taskInfo.getData().getOnceTask();
                FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData nowDayTask = taskInfo.getData().getDayTask();

                String sealRewardStr = getSealRewardTask(Integer.parseInt(onceTask.getSealTotal()));
                if (nowDayTask.likeSealComplete() && nowDayTask.commentSealComplete() && nowDayTask.signSealComplete()) {
                    return String.format("盖章奖励: %s , 盖章印记: %s", sealRewardStr, onceTask.getSealTotal());
                }
            }
        }
        return "已结束或已完成";
    }

    public String checkThenGetSignRewardTask() {
        FF14MySignLogResponse ff14MySignLogResponse = ff14ForumRequest.mySignLog();
        if (ff14MySignLogResponse.isSuccess()) {
            int count = ff14MySignLogResponse.getData().getCount();
            FF14SignRewardListResponse ff14SignRewardListResponse = ff14ForumRequest.signRewardList();
            if (ff14SignRewardListResponse.isSuccess()) {
                for (FF14SignRewardListResponse.SignRewardListData datum : ff14ForumRequest.signRewardList().getData()) {
                    if (count >= datum.getRule()) {
                        if (datum.getIsGet() == 1) {
                            continue;
                        }

                    }

                }
            }
        }
        return "";
    }


    public int signInTask() {
        FF14MySignLogResponse ff14MySignLogResponse = ff14ForumRequest.mySignLog();
        int signInDay = 0;
        if (ff14ForumRequest.mySignLog().isSuccess()) {
            FF14MySignLogResponse.MySignLogData data = ff14MySignLogResponse.getData();
            signInDay = data.getCount();
            boolean todaySignIn = false;
            if (signInDay > 0) {
                int size = data.getRows().size();
                FF14MySignLogResponse.MySignLogData.MySignLogRowData mySignLogRowData = data.getRows().get(size - 1);
                SimpleDateFormat simpleDateFormatSignTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat simpleDateFormatYearMonth = new SimpleDateFormat("MMdd");
                try {
                    Date parse = simpleDateFormatSignTime.parse(mySignLogRowData.getSignTime());
                    String signInDate = simpleDateFormatYearMonth.format(parse);
                    String nowDate = simpleDateFormatYearMonth.format(new Date());
                    if (!signInDate.equalsIgnoreCase(nowDate)) {
                        todaySignIn = true;
                    }
                } catch (ParseException e) {
                    Notifications.sendAllNotification("意外:signInTask", "时间解析失败");
                    throw new RuntimeException(e);
                }
            } else {
                todaySignIn = true;
            }
            if (todaySignIn) {
                FF14SignInResponse ff14SignInResponse = ff14ForumRequest.signIn();
                if (ff14SignInResponse.isSuccess()) {
                    signInDay = Integer.parseInt(ff14SignInResponse.getData().getTotalDays());
                }
            }
        }
        return signInDay;

    }


    /**
     * 领取印章奖励
     *
     * @param num = 当前印章次数
     * @return 获取的奖励
     */
    public String getSealRewardTask(int num) {
        StringBuilder sealRewardSb = new StringBuilder();
        FF14ItemListResponse ff14ItemListResponse = ff14ForumRequest.itemList();
        if (ff14ItemListResponse.isSuccess()) {
            List<FF14ItemListResponse.FF14ItemListData> ff14ItemListData = ff14ItemListResponse.getData();
            for (int i = 1; i < ff14ItemListData.size(); i++) {
                if (!ff14ItemListData.get(i).isGet()) {
                    FF14ItemListResponse.FF14ItemListData item = ff14ItemListData.get(i);
                    int type = item.getType();
                    if (type <= num) {
                        FF14Response sealReward = ff14ForumRequest.getSealReward(type + "");
                        if (sealReward.isSuccess()) {
                            sealRewardSb.append(item.getNote()).append("-").append(item.getItemName()).append("===");
                        }
                    }
                }
            }
        }
        if (sealRewardSb.isEmpty()) {
            sealRewardSb.append("无");
        }
        return sealRewardSb.toString();
    }
}
