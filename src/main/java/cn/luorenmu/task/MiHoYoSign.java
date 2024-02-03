package cn.luorenmu.task;


import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.request.mihoyo.MihoyoForumRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author LoMu
 * Date 2023.10.28 18:47
 */

@Slf4j
public class MiHoYoSign {
    private static ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    MihoyoTask mihoyoTask = new MihoyoTask();
    public MiHoYoSign() {
        AtomicInteger atomicInteger = new AtomicInteger(RunStorage.getConfig(Setting.class).getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(atomicInteger.intValue(), runnable -> new Thread(runnable, atomicInteger.decrementAndGet() + ""));
    }

    public void isRecentArticleTask() {
        MihoyoForumRequest mihoyoForumRequest = new MihoyoForumRequest();
        List<Setting.Account> accounts = RunStorage.getConfig(Setting.class).getAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
                Setting.Account account = RunStorage.getConfig(Setting.class).getAccounts().get(Integer.parseInt(Thread.currentThread().getName()));
                RunStorage.accountThreadLocal.set(account);
                try {
                    log.info("当前{}线程托管：{}", Thread.currentThread().getName(), RunStorage.accountThreadLocal.get().getNotification().getEmail());
                    mihoyoTask.isRecentArticle();
                } catch (Exception e) {
                    log.error("发生错误 : {}", Arrays.toString(e.getStackTrace()));
                }

            }, 0, 1, TimeUnit.HOURS);
        }

        log.info("版本兑换码监测已运行");
    }


}
