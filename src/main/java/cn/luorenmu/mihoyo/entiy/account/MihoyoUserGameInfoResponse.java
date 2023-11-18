package cn.luorenmu.mihoyo.entiy.account;

import cn.luorenmu.mihoyo.entiy.MihoyoResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.11.05 18:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MihoyoUserGameInfoResponse extends MihoyoResponse {

    @Data
    public static class UserGameInfoData {
        private String gameBiz;
        private String region;
        private String gameUid;
        private String nickname;
        private int level;
        private boolean isChosen;
        private String regionName;
        private boolean isOfficial;
    }
}
