package cn.luorenmu.common.utils;

import cn.hutool.core.lang.ClassScanner;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author LoMu
 * Date 2023.12.20 1:26
 */
@Slf4j
public class ReflexUtils {
    public static void scanAnnotationThenConsumer(Class<?> classz, Class<? extends Annotation> annotationClass, Consumer<Field> consumer) {
        Set<Class<?>> classes = ClassScanner.scanPackage(classz.getPackageName());
        for (Class<?> aClass : classes) {
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.isAnnotationPresent(annotationClass)) {
                    consumer.accept(declaredField);
                }
            }
        }
    }
}
