package cn.luorenmu.common.annotation.impl;

import cn.hutool.core.lang.ClassScanner;
import cn.luorenmu.Main;
import cn.luorenmu.common.annotation.CurrentSetting;
import cn.luorenmu.common.file.ReadWriteFile;
import cn.luorenmu.config.Setting;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.timer.TaskHandleCenter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author LoMu
 * Date 2024.01.26 20:01
 */
@Slf4j
public class CurrentSettingImpl {

    public static void scanSettingInject(Class<?> classz) {
        Set<Class<?>> classes = ClassScanner.scanPackage(classz.getPackageName());
        for (Class<?> aClass : classes) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(CurrentSetting.class)) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(null, RunStorage.getConfig(Setting.class));
                    } catch (IllegalAccessException e) {
                        log.error("CurrentSetting 字段无法正常注入 class: {}, field: {} ", aClass.getName(), declaredField.getName());
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        RunStorage.CONFIG_ENITYS = ReadWriteFile.initConfig(true);
        scanSettingInject(Main.class);
        new TaskHandleCenter().execute();
    }
}
