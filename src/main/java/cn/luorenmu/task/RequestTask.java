package cn.luorenmu.task;

import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.request.ff14.FF14ForumRequest;
import cn.luorenmu.request.ff14.entiy.FF14MySignLogResponse;
import cn.luorenmu.request.ff14.entiy.FF14MyTaskInfoResponse;
import cn.luorenmu.request.ff14.entiy.FF14Response;
import cn.luorenmu.request.ff14.entiy.FF14SignInResponse;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            FF14MySignLogResponse ff14MySignLogResponse = ff14ForumRequest.mySignLog();
            String signTime = ff14MySignLogResponse.getData().getRows().get(0).getSignTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = null;
            try {
                parse = simpleDateFormat.parse(signTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMdd");
            int sign = Integer.parseInt(simpleDateFormat1.format(parse));
            int now = Integer.parseInt(simpleDateFormat1.format(new Date()));
            if (now > sign) {
                ff14ForumRequest.signIn();
            }
            FF14MyTaskInfoResponse ff14MyTaskInfoResponse = ff14ForumRequest.myTaskInfo();
            FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData dayTask = ff14MyTaskInfoResponse.getData().getDayTask();
            int likeNum = dayTask.getLikeNum();
            int execute = 5 - likeNum;
            if (execute != 0) {
                for (int i = 0; i < execute * 2 + 1; i++) {
                    ff14ForumRequest.like();
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (dayTask.getLikeSeal() != 1) {
                ff14ForumRequest.doSeal("2");
            }

            if (dayTask.getCommentSeal() != 1) {
                FF14Response comment = ff14ForumRequest.comment();
                if (comment.getCode() != 10000) {
                    Notifications.sendAllNotification("意外:FF14评论失败", comment.getMsg());
                } else {
                    ff14ForumRequest.doSeal("3");
                }
            }

            if (dayTask.getSignSeal() != 1) {
                if (dayTask.getSignStatus() != 1) {
                    FF14SignInResponse ff14SignInResponse = ff14ForumRequest.signIn();
                    if (ff14SignInResponse.getCode() != 10000) {
                        Notifications.sendAllNotification("意外：签到失败", ff14SignInResponse.getMsg());
                    }
                    ff14ForumRequest.doSeal("1");
                }
            }
            FF14MyTaskInfoResponse taskInfo = ff14ForumRequest.myTaskInfo();
            FF14MySignLogResponse signLog = ff14ForumRequest.mySignLog();
            FF14MySignLogResponse.MySignLogData.MySignLogRowData mySignLogRowData = signLog.getData().getRows().get(0);
            FF14MyTaskInfoResponse.MyTaskInfoData.MyTaskInfoDayTaskData nowDayTask = taskInfo.getData().getDayTask();
            if (nowDayTask.getLikeSeal() == 1 && nowDayTask.getCommentSeal() == 1 && nowDayTask.getSignSeal() == 1) {
                Notifications.sendAllNotification("FF14签到已完成", "角色:" + mySignLogRowData.getCharacterName() + "  当前已签到: " + mySignLogRowData.getPlatform());
            }
        }, 0, 24, TimeUnit.HOURS);
    }
}
