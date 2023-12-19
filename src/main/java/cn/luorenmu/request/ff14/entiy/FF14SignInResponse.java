package cn.luorenmu.request.ff14.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.12.19 17:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14SignInResponse extends FF14Response {
    private FF14SignInData data;

    @Data
    public static class FF14SignInData {
        private int continuousDays;
        private int shopExp;
        private String sqMsg;
        private String totalDays;
    }
}
