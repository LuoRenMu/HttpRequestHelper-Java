package cn.luorenmu.common.annotation.impl;


import cn.hutool.core.lang.ClassScanner;
import cn.luorenmu.Main;
import cn.luorenmu.common.annotation.GenerationFile;
import cn.luorenmu.common.utils.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;


/**
 * @author LoMu
 * Date 2023.12.08 19:47
 */
public class ValueIMPL {


    public static void main(String[] args) throws Exception {
        ClassScanner annotationScan = new ClassScanner(GenerationFile.class.getPackageName());
        Set<Class<?>> annotationClass = annotationScan.scan();
        ClassScanner mainScan = new ClassScanner(Main.class.getPackageName());
        Set<Class<?>> allClasses = mainScan.scan();
        for (Class<?> aClass : annotationClass) {
            if (aClass.isInterface()) {
                Target annotation = aClass.getAnnotation(Target.class);
                for (Class<?> bClass : allClasses) {
                    for (ElementType elementType : annotation.value()) {
                        Method method = Class.class.getMethod("getDeclared" + StringUtils.firstCharacterUpperCaseOtherLowerCase(elementType.name() + "s"));
                        method.setAccessible(true);
                        Object[] invoke = (Object[]) method.invoke(bClass);
                        for (Object o : invoke) {
                            if (o instanceof Field field) {
                                for (Annotation fieldAnnotation : field.getAnnotations()) {
                                    if (fieldAnnotation.annotationType() == aClass) {
                                        field.setAccessible(true);

                                        System.out.println(fieldAnnotation);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }


    }

}
