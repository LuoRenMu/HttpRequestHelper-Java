package cn.luorenmu.request.ff14.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2024.01.18 20:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14CharacterBindInfoResponse extends FF14Response {
    private FF14CharacterBindInfoData data;

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class FF14CharacterBindInfoData {
        private long uuid;
    }
}
