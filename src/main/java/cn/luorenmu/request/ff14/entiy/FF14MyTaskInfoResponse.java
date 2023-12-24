package cn.luorenmu.request.ff14.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.12.19 18:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14MyTaskInfoResponse extends FF14Response {
    private MyTaskInfoData data;

    @Data
    public static class MyTaskInfoData {
        private MyTaskInfoOnceTaskData onceTask;
        private MyTaskInfoDayTaskData dayTask;

        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        @Data
        public static class MyTaskInfoOnceTaskData {
            private String sealTotal;
        }

        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        @Data
        public static class MyTaskInfoDayTaskData {
            private int signStatus;
            private int signSeal;
            private int likeNum;
            private int likeSeal;
            private int commentStatus;
            private int commentSeal;

            public boolean likeSealComplete() {
                return likeSeal == 1;
            }

            public boolean signSealComplete() {
                return signSeal == 1;
            }

            public boolean commentSealComplete() {
                return commentSeal == 1;
            }

        }
    }
}
