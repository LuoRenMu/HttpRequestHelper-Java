package cn.luorenmu.timer;

import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LoMu
 * Date 2024.01.25 18:55
 */
public class TaskHandleCenter {
    private static ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    @CurrentSetting
    private static Setting setting;

    public TaskHandleCenter() {
        AtomicInteger atomicInteger = new AtomicInteger(RunStorage.getConfig(Setting.class).getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(atomicInteger.intValue(), runnable -> new Thread(runnable, atomicInteger.decrementAndGet() + ""));
    }


    public void execute() {
        System.out.println(setting);
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {

        }, 0, 5L, TimeUnit.MINUTES);
    }
}
