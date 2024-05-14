package cn.luorenmu.request.ff14.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.12.20 22:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14IsLoginResponse extends FF14Response {
    private List<FF14IsLoginData> data;

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class FF14IsLoginData {
        private String areaName;
        private String characterName;
        private String groupName;
    }
}
