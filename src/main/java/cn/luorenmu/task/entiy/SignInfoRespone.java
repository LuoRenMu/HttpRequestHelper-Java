package cn.luorenmu.task.entiy;

import lombok.Data;


/**
 * @author LoMu
 * Date 2023.10.28 20:17
 */
@Data
public class SignInfoRespone {
    private int retcode;
    private String message;

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
