package cn.luorenmu.mihoyo.entiy.account;

import cn.luorenmu.mihoyo.entiy.MihoyoResponse;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
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
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class UserTokenData {
        private String uid;
        private String cookieToken;
    }

}
