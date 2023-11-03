package cn.luorenmu.task.entiy;

import lombok.Data;

/**
 * @author LoMu
 * Date 2023.10.30 22:30
 */
@Data
public class SignRespone {
    private int retcode;
    private String message;
    private SignResponseData data;

    @Data
    public static class SignResponseData {
        private int code;
        private String riskCode;
        //验证码
        private String gt;
        private String challenge;
        private int success;
        private boolean isRisk;

    }
}
