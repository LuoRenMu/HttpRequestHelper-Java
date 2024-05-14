package cn.luorenmu.config;

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
    private List<Account> accounts;
    private General general;


    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class General {
        private MailAccount mail;
        private String serverChanUrl;

    }


    @Data
    public static class Account {
        private MihoyoAccount mihoyo;
        private AccountNotification notification;
        private FF14Account ff14;

        @Data
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        public static class FF14Account {
            private String cookie;
        }

        @Data
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        public static class MihoyoAccount {
            private String cookie;
            private SToken sToken;
            private String account;
            private String password;

            public void setsToken(String sToken) {
                if (sToken.isBlank()) {
                    return;
                }
                this.sToken = new SToken();
                Automatic.AutoSetFieldFunctionContains(this.sToken, String.class, sToken, String.class, s -> s.split(";"));
            }

            @Override
            public String toString() {
                return "MihoyoAccount{cookie='******', sToken='******', account='" + account + "', password='******'}";
            }
        }

        @Override
        public String toString() {
            return "Account{\n" +
                    "mihoyo=" + mihoyo +
                    "\nnotification=" + notification +
                    "\nff14=" + ff14 +
                    "\n}";
        }
    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class AccountNotification {
        private String serverChanKey;
        private String email;
    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class SToken {
        private String stuid;
        private String stoken;
        private String mid;
        private String sTokenStr;

    }
}
