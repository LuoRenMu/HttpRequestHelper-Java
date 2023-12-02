package cn.luorenmu.common.auto;

import cn.luorenmu.common.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author LoMu
 * Date 2023.11.04 15:03
 */

//该类用于将字符串自动装配到对象
public class Automatic {
    public static void AutoSetFieldEquals(Object object, Class<?> classo, String fieldName, Class<?> classa, Object... args) {
        Field[] fields = classo.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                try {
                    String methodName = "set" + StringUtil.firstCharacterUpperCase(fieldName);
                    Method method = classo.getMethod(methodName, classa);
                    method.invoke(object, args);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void AutoSetFieldFunctionContains(Object object, Class<?> classo, String args, Class<?> classa, Function<String, String[]> function) {
        Field[] fields = classo.getDeclaredFields();
        String[] split = function.apply(args);

        assert split != null;
        for (String s : split) {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (s.contains(fieldName)) {
                    try {
                        String param = s.substring(s.indexOf("=") + 1);
                        String methodName = "set" + StringUtil.firstCharacterUpperCase(fieldName);
                        Method method = classo.getMethod(methodName, classa);
                        method.invoke(object, param);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
