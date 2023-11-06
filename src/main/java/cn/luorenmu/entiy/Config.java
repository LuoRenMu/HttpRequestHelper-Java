package cn.luorenmu.entiy;

import cn.luorenmu.common.auto.Automatic;
import lombok.Data;


/**
 * @author LoMu
 * Date 2023.10.28 19:36
 */


@Data
public class Config {
    private SToken sToken;
    private String serverChanKey;
    private String serverChanUrl;
    private String email;
    private String cookie;

    public void setSToken(String sTokenStr) {
        sToken = new SToken();
        sToken.setSTokenStr(sTokenStr);
        Automatic.AutoSetFieldFunctionContains(sToken, SToken.class, sTokenStr, String.class, args -> {
            String[] split = null;
            if (args != null) {
                if (args.contains(",")) {
                    split = args.split(",");
                } else if (args.contains(";")) {
                    split = args.split(";");
                }
            }
            return split;
        });
    }

    @Data
    public static class SToken {
        private String stuid;
        private String stoken;
        private String mid;
        private String sTokenStr;

    }
}
