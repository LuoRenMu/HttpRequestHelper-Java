package cn.luorenmu.task.entiy;

import lombok.Data;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.10.29 13:55
 */
@Data
public class SignRewardInfo {
    private int retcode;
    private String message;
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
