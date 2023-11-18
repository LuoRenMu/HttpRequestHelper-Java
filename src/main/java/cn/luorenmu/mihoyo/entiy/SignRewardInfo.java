package cn.luorenmu.mihoyo.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.10.29 13:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignRewardInfo extends MihoyoResponse {
    private GameData data;


    @lombok.Data
    public static class GameData {
        private int month;
        private String biz;
        private boolean resign;
        private List<Award> awards;


        @lombok.Data
        public static class ShortExtraAward {
            private boolean hasExtraAward;
            private String startTime;
            private String endTime;
            private List<String> list;
            private String startTimestamp;
            private String endTimestamp;
        }

        @lombok.Data
        public static class Award {
            private String icon;
            private String name;
            private int cnt;
        }
    }
}
