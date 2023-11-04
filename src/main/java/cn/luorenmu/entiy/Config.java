package cn.luorenmu.entiy;

import cn.luorenmu.common.auto.Automatic;
import lombok.Data;


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
        cookie = new Cookie();
        cookie.setCookieStr(cookieStr);
        Automatic.AutoSetFieldSplitContains(cookie, Cookie.class, cookieStr, String.class);
    }

    @Data
    public static class Cookie {
        private String stuid;
        private String stoken;
        private String mid;
        private String cookieStr;

    }
}
