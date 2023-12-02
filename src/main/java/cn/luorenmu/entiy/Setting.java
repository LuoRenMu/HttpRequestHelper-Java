package cn.luorenmu.entiy;

import cn.hutool.extra.mail.MailAccount;
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
    private MihoyoSetting mihoyo;
    private General general;



    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class General {
        private MailAccount mail;
        private String serverChanUrl;
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
            private SToken sToken;
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
