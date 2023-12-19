package cn.luorenmu.request.mihoyo.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.10.30 22:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignRespone extends MihoyoResponse {
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
