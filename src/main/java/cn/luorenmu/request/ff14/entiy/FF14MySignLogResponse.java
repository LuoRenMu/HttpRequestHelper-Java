package cn.luorenmu.request.ff14.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.12.19 18:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14MySignLogResponse extends FF14Response {
    private MySignLogData data;

    @Data
    public static class MySignLogData {
        private int count;
        private List<MySignLogRowData> rows;

        @Data
        @JSONType(naming = PropertyNamingStrategy.SnakeCase)
        public static class MySignLogRowData {
            private String areaName;
            private String characterName;
            private String groupName;
            private String id;
            private String ipLocation;
            private int platform;
            private String signTime;
            private String uuid;
        }
    }
}
