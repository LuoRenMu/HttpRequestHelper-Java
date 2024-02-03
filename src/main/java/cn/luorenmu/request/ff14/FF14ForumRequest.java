package cn.luorenmu.request.ff14;

import cn.luorenmu.common.request.HttpRequest;
import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.config.Ff14Request;
import cn.luorenmu.entiy.Request;
import cn.luorenmu.entiy.RequestType;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.request.ff14.entiy.*;
import com.alibaba.fastjson2.JSON;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LoMu
 * Date 2023.12.19 16:37
 */
public class FF14ForumRequest {
    private static final Ff14Request request = RunStorage.getConfig(Ff14Request.class);


    protected static String ff14Request(Request.RequestDetailed requestDetailed, String... args) {
        requestDetailed.setRequestType(RequestType.FF14);
        return HttpRequest.execute(requestDetailed, args);
    }

    public FF14Response createDynamic() {
        return JSON.parseObject(ff14Request(request.getCreateDynamic()), FF14Response.class);
    }

    /**
     * 任务详情
     *
     * @return FF14MyTaskInfoResponse
     */
    public FF14MyTaskInfoResponse myTaskInfo() {
        return JSON.parseObject(ff14Request(request.getMyTaskInfo()), FF14MyTaskInfoResponse.class);

    }

    /**
     * 评论
     *
     * @return FF14Response
     */
    public FF14Response comment() {
        return JSON.parseObject(ff14Request(request.getComment()), FF14Response.class);
    }

    /**
     * 印章(任务完成)
     *
     * @param num 1 = 签到  2 = 点赞*5   3 = 评论
     * @return FF14Response
     */
    public FF14Response doSeal(String num) {
        return JSON.parseObject(ff14Request(request.getDoSeal(), num), FF14Response.class);
    }

    /**
     * 获取印章奖励
     *
     * @param num 印章天数(signRewardList)
     * @return FF14Response
     */
    public FF14Response getSealReward(String num) {
        return JSON.parseObject(ff14Request(request.getGetSealReward(), num), FF14Response.class);

    }

    /**
     * 点赞/取消点赞
     *
     * @return FF14Response
     */
    public FF14Response like() {
        return JSON.parseObject(ff14Request(request.getLike()), FF14LikeResponse.class);

    }

    /**
     * 印章奖励查询
     *
     * @return FF14ItemListResponse
     */
    public FF14ItemListResponse itemList() {
        return JSON.parseObject(ff14Request(request.getItemList()), FF14ItemListResponse.class);
    }

    /**
     * 领取印章奖励
     *
     * @param num = 当前印章次数
     * @return 获取的奖励
     */
    public String getSealRewardTask(int num) {
        StringBuilder sealRewardSb = new StringBuilder();
        FF14ItemListResponse ff14ItemListResponse = itemList();
        if (ff14ItemListResponse.isSuccess()) {
            List<FF14ItemListResponse.FF14ItemListData> ff14ItemListData = ff14ItemListResponse.getData();
            for (int i = 1; i < ff14ItemListData.size(); i++) {
                if (!ff14ItemListData.get(i).isGet()) {
                    FF14ItemListResponse.FF14ItemListData item = ff14ItemListData.get(i);
                    int type = item.getType();
                    if (type <= num) {
                        FF14Response sealReward = getSealReward(type + "");
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

    /**
     * 签到日志
     *
     * @return FF14MySignLogResponse
     */
    public FF14MySignLogResponse mySignLog() {
        return JSON.parseObject(ff14Request(request.getMySignLog()), FF14MySignLogResponse.class);

    }

    /**
     * 查看签到奖励
     *
     * @return FF14SignRewardListResponse
     */
    public FF14SignRewardListResponse signRewardList() {
        return JSON.parseObject(ff14Request(request.getSignRewardList()), FF14SignRewardListResponse.class);


    }

    /**
     * 签到
     *
     * @return FF14SignInResponse
     */
    public FF14SignInResponse signIn() {
        return JSON.parseObject(ff14Request(request.getSignIn()), FF14SignInResponse.class);

    }

    public void likeTask(int num) {
        int execute = 5 - num;
        if (execute != 0) {
            for (int i = 0; i < execute * 2 + 1; i++) {
                FF14Response like = like();
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
        FF14MyTaskInfoResponse ff14MyTaskInfoResponse = myTaskInfo();
        if (ff14MyTaskInfoResponse.isSuccess()) {
            FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData sealDayTask = ff14MyTaskInfoResponse.getData().getDayTask();
            likeTask(sealDayTask.getLikeNum());

            if (!sealDayTask.signSealComplete()) {
                if (sealDayTask.getSignStatus() != 1) {
                    FF14SignInResponse ff14SignInResponse = signIn();
                    ff14SignInResponse.isSuccess();
                }
                doSeal("1");
            }

            if (!sealDayTask.likeSealComplete()) {
                doSeal("2");
            }

            if (!sealDayTask.commentSealComplete()) {
                if (sealDayTask.getCommentSeal() > 0) {
                    doSeal("3");
                } else {
                    FF14Response comment = comment();
                    if (comment.isSuccess()) {
                        doSeal("3");
                    }
                }
            }
            FF14MyTaskInfoResponse taskInfo = myTaskInfo();
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
        FF14MySignLogResponse ff14MySignLogResponse = mySignLog();
        if (ff14MySignLogResponse.isSuccess()) {
            int count = ff14MySignLogResponse.getData().getCount();
            FF14SignRewardListResponse ff14SignRewardListResponse = signRewardList();
            if (ff14SignRewardListResponse.isSuccess()) {
                for (FF14SignRewardListResponse.SignRewardListData datum : signRewardList().getData()) {
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
        FF14MySignLogResponse ff14MySignLogResponse = mySignLog();
        int signInDay = 0;
        if (mySignLog().isSuccess()) {
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
                FF14SignInResponse ff14SignInResponse = signIn();
                if (ff14SignInResponse.isSuccess()) {
                    signInDay = Integer.parseInt(ff14SignInResponse.getData().getTotalDays());
                }
            }
        }
        return signInDay;

    }
}
