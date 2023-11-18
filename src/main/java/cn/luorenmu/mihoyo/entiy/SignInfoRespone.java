package cn.luorenmu.mihoyo.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author LoMu
 * Date 2023.10.28 20:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignInfoRespone extends MihoyoResponse {
    private Data data;

    @lombok.Data
    public static class Data {
        private int totalSignDay;
        private String today;
        private boolean isSign;
        private boolean isSub;
        private String region;
        private int signCntMissed;
        private int shortSignDay;
    }
}
