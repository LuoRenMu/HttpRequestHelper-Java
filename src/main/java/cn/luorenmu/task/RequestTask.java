package cn.luorenmu.task;

import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.request.ff14.FF14AccountRequest;
import cn.luorenmu.request.ff14.FF14ForumRequest;
import cn.luorenmu.request.ff14.entiy.FF14IsLoginResponse;
import cn.luorenmu.request.ff14.entiy.FF14MyTaskInfoResponse;
import cn.luorenmu.request.ff14.entiy.FF14Response;
import cn.luorenmu.request.ff14.entiy.FF14SignInResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LoMu
 * Date 2023.12.10 1:09
 */

@Slf4j
public class RequestTask {
    private final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    private final AtomicInteger integer = new AtomicInteger();

    public RequestTask() {
        Setting setting = FileManager.getConfig(Setting.class);
        integer.set(setting.getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(integer.get(), runnable -> new Thread(runnable, integer.decrementAndGet() + ""));

    }

    public void execute() {
        log.info("FF14自动操作已运行");
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {

            Setting.Account account = FileManager.getConfig(Setting.class).getAccounts().get(Integer.parseInt(Thread.currentThread().getName()));
            RunningStorage.accountThreadLocal.set(account);
            FF14ForumRequest ff14ForumRequest = new FF14ForumRequest();
            FF14MyTaskInfoResponse ff14MyTaskInfoResponse = ff14ForumRequest.myTaskInfo();
            FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData dayTask = ff14MyTaskInfoResponse.getData().getDayTask();
            FF14AccountRequest ff14AccountRequest = new FF14AccountRequest();
            FF14IsLoginResponse login = ff14AccountRequest.isLogin();
            if (login.isSuccess()) {
                int signInDay = ff14ForumRequest.signInTask();
                ff14ForumRequest.likeTask(dayTask.getLikeNum());

                if (!dayTask.signSealComplete()) {
                    if (dayTask.getSignStatus() != 1) {
                        FF14SignInResponse ff14SignInResponse = ff14ForumRequest.signIn();
                        ff14SignInResponse.isSuccess();
                    }
                    ff14ForumRequest.doSeal("1");
                }

                if (!dayTask.likeSealComplete()) {
                    ff14ForumRequest.doSeal("2");
                }

                if (!dayTask.commentSealComplete()) {
                    FF14Response comment = ff14ForumRequest.comment();
                    if (comment.isSuccess()) {
                        ff14ForumRequest.doSeal("3");
                    }
                }


                FF14MyTaskInfoResponse taskInfo = ff14ForumRequest.myTaskInfo();
                FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoOnceTaskData onceTask = taskInfo.getData().getOnceTask();
                FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData nowDayTask = taskInfo.getData().getDayTask();

                FF14IsLoginResponse.FF14IsLoginData loginData = login.getData();
                String sealRewardStr = ff14ForumRequest.getSealRewardTask(Integer.parseInt(onceTask.getSealTotal()));
                if (nowDayTask.likeSealComplete() && nowDayTask.commentSealComplete() && nowDayTask.signSealComplete()) {
                    Notifications.sendAllNotification("FF14签到已完成", String.format("当前角色: %s-%s , 盖章已完成 今日盖章奖励 %s , 已盖章: %s , 已签到: %s"
                            , loginData.getGroupName(), loginData.getCharacterName(), sealRewardStr, onceTask.getSealTotal(), signInDay));
                } else {
                    Notifications.sendAllNotification("FF14签到已完成", String.format("当前角色: %s:%s ,已签到: %s 盖章未完成或已结束"
                            , loginData.getGroupName(), loginData.getCharacterName(), signInDay));
                }
            }
        }, 0, 24, TimeUnit.HOURS);
    }
}
