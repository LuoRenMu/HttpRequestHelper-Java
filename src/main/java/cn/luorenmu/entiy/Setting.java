package cn.luorenmu.entiy;

import cn.hutool.extra.mail.MailAccount;
import cn.luorenmu.common.auto.Automatic;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.List;


/**
 * @author LoMu
 * Date 2023.10.28 19:36
 */


@Data
public class Setting {
    private SToken sToken;
    private String serverChanKey;
    private String serverChanUrl;
    private String email;
    private String cookie;
    private MihoyoSetting mihoyo;
    private General general;



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
    public static class General {
        private MailAccount mail;
    }


    @Data
    public static class MihoyoSetting {
        private List<MihoyoSettingAccount> accounts;

        @Data
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        public static class MihoyoSettingAccount {
            private String cookie;
            private String serverChanKey;
            private String email;
        }
    }

    @Data
    public static class SToken {
        private String stuid;
        private String stoken;
        private String mid;
        private String sTokenStr;

    }
}
