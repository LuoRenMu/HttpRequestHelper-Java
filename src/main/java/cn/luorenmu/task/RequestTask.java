package cn.luorenmu.task;

import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.request.ff14.FF14AccountRequest;
import cn.luorenmu.request.ff14.FF14ForumRequest;
import cn.luorenmu.request.ff14.entiy.FF14IsLoginResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
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


    public RequestTask() {
        AtomicInteger integer = new AtomicInteger(RunStorage.getConfig(Setting.class).getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(integer.get(), runnable -> new Thread(runnable, integer.decrementAndGet() + ""));

    }

    public void ff14Task() {
        try {
            Setting.Account account = RunStorage.getConfig(Setting.class).getAccounts().get(Integer.parseInt(Thread.currentThread().getName()));
            RunStorage.accountThreadLocal.set(account);

            if (RunStorage.accountThreadLocal.get().getFf14() == null || RunStorage.accountThreadLocal.get().getFf14().getCookie().isBlank()) {
                return;
            }

            FF14ForumRequest ff14ForumRequest = new FF14ForumRequest();
            FF14AccountRequest ff14AccountRequest = new FF14AccountRequest();


            FF14IsLoginResponse login = ff14AccountRequest.isLogin();
            if (login.isSuccess()) {
                String sealRewardStr = ff14ForumRequest.sealTask();
                int signInDay = ff14ForumRequest.signInTask();
                ff14ForumRequest.comment();
                ff14ForumRequest.createDynamic();


                FF14IsLoginResponse.FF14IsLoginData loginData = login.getData();

                Notifications.sendAllNotification("FF14签到已完成", String.format("当前角色: %s:%s ,已签到: %s  盖章: %s"
                        , loginData.getGroupName(), loginData.getCharacterName(), signInDay, sealRewardStr));

            }
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public void execute() {
        log.info("FF14自动操作已运行");
        List<Setting.Account> accounts = RunStorage.getConfig(Setting.class).getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(this::ff14Task, 0, 24, TimeUnit.HOURS);
        }
    }
}
