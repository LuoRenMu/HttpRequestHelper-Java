package cn.luorenmu.common.annotation.impl;

import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.common.utils.ReflexUtils;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import lombok.extern.slf4j.Slf4j;


/**
 * @author LoMu
 * Date 2024.01.26 20:01
 */
@Slf4j
public class CurrentSettingImpl {

    public static void scanSettingInject(Class<?> classz) {
        ReflexUtils.scanAnnotationThenConsumer(classz, CurrentSetting.class, f -> {
            f.setAccessible(true);
            try {
                f.set(null, RunStorage.getConfig(Setting.class));
            } catch (IllegalAccessException e) {
                log.error("CurrentSetting 字段无法正常注入 class: {}, field: {} ", f.getDeclaringClass(), f.getName());
                throw new RuntimeException(e);
            }
        });
    }
}
