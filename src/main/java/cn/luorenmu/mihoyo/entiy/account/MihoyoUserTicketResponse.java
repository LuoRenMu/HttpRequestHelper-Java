package cn.luorenmu.mihoyo.entiy.account;

import cn.luorenmu.mihoyo.entiy.MihoyoResponse;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.11.05 17:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MihoyoUserTicketResponse extends MihoyoResponse {

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class ActionTicketBySTokenData {
        private String ticket;
        private boolean isVerified;

        @Data
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        public static class TicketAccountInfo {
            private String accountId;
            private String accountName;
            private String email;
            private boolean isEmailVerify;
            private String createTime;
        }
    }
}
