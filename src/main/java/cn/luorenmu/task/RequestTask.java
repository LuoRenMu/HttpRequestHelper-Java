package cn.luorenmu.task;

import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Setting;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LoMu
 * Date 2023.12.10 1:09
 */
public class RequestTask {
    private final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;
    private final Setting setting;
    private final AtomicInteger integer = new AtomicInteger();

    public RequestTask() {
        setting = FileManager.getConfig(Setting.class);
        integer.set(setting.getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(integer.get(), runnable -> new Thread(runnable, integer.getAndDecrement() + ""));
    }

    public void execute() {

    }
}
