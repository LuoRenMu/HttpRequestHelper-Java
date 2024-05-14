package cn.luorenmu.timer;

import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.common.cache.FileCache;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.entiy.TaskCache;
import cn.luorenmu.task.FF14Task;
import cn.luorenmu.task.MihoyoTask;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author LoMu
 * Date 2024.01.25 18:55
 */

@Slf4j
public class TaskHandleCenter {
    @CurrentSetting
    private static Setting setting;
    private static final List<Thread> threads = new ArrayList<>();
    private final Thread monitor;
    MihoyoTask mihoyoTask = new MihoyoTask();
    FF14Task ff14Task = new FF14Task();
    private LocalDateTime lastUpdateTime;


    public TaskHandleCenter() {
        AtomicInteger atomicInteger = new AtomicInteger(RunStorage.getConfig(Setting.class).getAccounts().size());
        for (int i = 0; i < atomicInteger.get(); i++) {
            log.info("当前线程:{} 托管账户 -> {}", i, setting.getAccounts().get(i));
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        log.warn("Thread.{}: SLEEPING -> RUNNING", Thread.currentThread().getName());
                    }
                    task();
                }

            }, i + "");
            threads.add(thread);
        }
        monitor = new Thread(() -> {
            Map<String, Integer> recordsCount = new HashMap<>();
            Map<String, Integer> stackHashCode = new HashMap<>();
            while (true) {
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < threads.size(); i++) {
                    Thread thread = threads.get(i);
                    String name = thread.getName();
                    StackTraceElement[] stackTrace = thread.getStackTrace();
                    int tempStackHashCode = Arrays.hashCode(stackTrace);
                    if (stackHashCode.getOrDefault(name, 0) == tempStackHashCode) {
                        int count = recordsCount.getOrDefault(name, 0);
                        if (count >= 3) {
                            threadReplaceThenCreateNewThreadRunning(thread, i);
                            log.warn("线程.{} 处于方法|{}|太长时间已被其他线程替代", Arrays.stream(stackTrace).map(StackTraceElement::getMethodName).toList(), name);
                            recordsCount.put(name, 0);
                        } else {
                            recordsCount.put(name, count + 1);
                        }
                    } else {
                        stackHashCode.put(name, tempStackHashCode);
                    }

                    if (thread.getState() == Thread.State.TERMINATED) {
                        threadReplaceThenCreateNewThreadRunning(thread, i);
                    }
                }
            }
        }, "Monitor");
    }

    private void threadReplaceThenCreateNewThreadRunning(Thread thread, int listIndex) {
        thread = new Thread(this::task, thread.getName());
        threads.set(listIndex, thread);
        thread.start();
        log.warn("线程.{} 因意外错误导致终止,已被监控线程重新唤醒", thread.getName());
    }

    private void task() {
        LocalDateTime now = LocalDateTime.now();

        TaskCache taskCache = FileCache.readCache(Thread.currentThread().getName());
        if (now.getHour() == 8) {
            if (taskCache.getFf14() == null || taskCache.getFf14().getDayOfMonth() != now.getDayOfMonth()) {
                ff14Task.forumTask();
            }
        } else {
            if (taskCache.getFf14() == null || taskCache.getFf14().getDayOfMonth() != now.getDayOfMonth()) {
                ff14Task.forumTask();
            }
        }

        if (lastUpdateTime == null || ChronoUnit.MINUTES.between(lastUpdateTime, now) > 10) {
            mihoyoTask.isRecentArticle();
            lastUpdateTime = LocalDateTime.now();
        }
    }

    public void execute() {
        for (Thread thread : threads) {
            thread.start();
        }
        monitor.start();
    }
}
