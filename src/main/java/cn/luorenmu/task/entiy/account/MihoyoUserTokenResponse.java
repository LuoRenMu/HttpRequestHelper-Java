package cn.luorenmu.task.entiy.account;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.11.05 16:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MihoyoUserTokenResponse extends MihoyoResponse {
    private UserTokenData data;

    @Data
    public static class UserTokenData {
        private String uid;
        @JSONField(name = "cookie_token")
        private String cookieToken;
    }

}
