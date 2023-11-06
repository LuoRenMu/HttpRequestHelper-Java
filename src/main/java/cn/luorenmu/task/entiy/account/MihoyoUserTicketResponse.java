package cn.luorenmu.task.entiy.account;

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
    public static class ActionTicketBySTokenData {
        private String ticket;
        private boolean isVerified;

        @Data
        public static class TicketAccountInfo {
            private String accountId;
            private String accountName;
            private String email;
            private boolean isEmailVerify;
            private String createTime;
        }
    }
}
