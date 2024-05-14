package cn.luorenmu.common.annotation.impl;


import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.common.annotation.ThreadAccount;
import cn.luorenmu.common.utils.ReflexUtils;
import cn.luorenmu.config.Setting;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LoMu
 * Date 2024.04.25 14:19
 */
@Slf4j
public class ThreadAccountImpl {
    @CurrentSetting
    private static Setting setting;

    public static void scanThreadAccountInject(Class<?> classz) {
        ReflexUtils.scanAnnotationThenConsumer(classz, ThreadAccount.class, f -> {
            f.setAccessible(true);
            try {
                f.set(null, setting
                        .getAccounts()
                        .get(
                                Integer.parseInt(Thread.currentThread().getName())
                        )
                );
            } catch (IllegalAccessException e) {
                log.error("ThreadAccount 字段无法正常注入 class: {}, field: {} ", f.getDeclaringClass(), f.getName());
                throw new RuntimeException(e);
            }
        });
    }
}
