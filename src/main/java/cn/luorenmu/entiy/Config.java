package cn.luorenmu.entiy;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * @author LoMu
 * Date 2023.10.28 19:36
 */


@Data
public class Config {
    private Cookie cookie;
    private String serverChanKey;
    private String serverChanUrl = "https://sctapi.ftqq.com/";
    private String email;

    public void setCookie(String cookieStr) {
        String[] split = cookieStr.split(";");
        Field[] fields = Cookie.class.getDeclaredFields();
        cookie = new Cookie();
        cookie.setCookieStr(cookieStr);
        for (String s : split) {
            for (Field field : fields) {
                String name = field.getName();
                if (s.contains(name)) {
                    String param = s.substring(s.indexOf("=") + 1);
                    String fristStr = String.valueOf(name.charAt(0)).toUpperCase();
                    String behindStr = name.substring(1);
                    try {
                        Cookie.class.getMethod("set" + fristStr + behindStr, String.class).invoke(cookie, param);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    @Data
    public static class Cookie {
        private String stuid;
        private String stoken;
        private String mid;
        private String cookieStr;

    }
}
