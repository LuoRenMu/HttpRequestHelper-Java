package cn.luorenmu.task.entiy;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.10.29 13:55
 */
public class SignInRewardInfo {
    private int retcode;
    private String message;


    public static class Data {
        private int month;
        private String biz;
        private boolean resign;
        private List<Award> awards;


        public static class ShortExtraAward {
            private boolean hasExtraAward;
            private String startTime;
            private String endTime;
            private List<String> list;
            private String startTimestamp;
            private String endTimestamp;
        }

        public static class Award {
            private String icon;
            private String name;
            private int cnt;
        }
    }
}
