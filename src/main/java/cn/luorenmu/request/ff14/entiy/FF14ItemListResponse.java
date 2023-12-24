package cn.luorenmu.request.ff14.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.12.20 22:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14ItemListResponse extends FF14Response {
    private List<FF14ItemListData> data;

    @Data
    public static class FF14ItemListData {
        private int integral;
        private int isGet;
        private String itemName;
        private String note;
        private int type;

        public boolean isGet() {
            return isGet == 1;
        }
    }
}
