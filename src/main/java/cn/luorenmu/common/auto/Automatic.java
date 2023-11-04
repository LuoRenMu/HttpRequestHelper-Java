package cn.luorenmu.common.auto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author LoMu
 * Date 2023.11.04 15:03
 */
public class Automatic {
    public static void AutoSetFieldEquals(Object object, Class<?> classo, String fieldName, Class<?> classa, Object... args) {
        Field[] fields = classo.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                try {
                    String fristStr = String.valueOf(fieldName.charAt(0)).toUpperCase();
                    String behindStr = fieldName.substring(1);
                    String methodName = "set" + fristStr + behindStr;
                    Method method = classo.getMethod(methodName, classa);
                    method.invoke(object, args);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void AutoSetFieldSplitContains(Object object, Class<?> classo, String args, Class<?> classa) {
        Field[] fields = classo.getDeclaredFields();
        String[] split = null;
        if (args != null) {
            if (args.contains(",")) {
                split = args.split(",");
            } else if (args.contains(";")) {
                split = args.split(";");
            }
        }

        assert split != null;
        for (String s : split) {
            for (Field field : fields) {
                String fieldName = field.getName();
                if (s.contains(fieldName)) {
                    try {
                        String param = s.substring(s.indexOf("=") + 1);
                        String fristStr = String.valueOf(fieldName.charAt(0)).toUpperCase();
                        String behindStr = fieldName.substring(1);
                        String methodName = "set" + fristStr + behindStr;
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
