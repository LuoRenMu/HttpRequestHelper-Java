package cn.luorenmu.request.mihoyo.entiy.account;

import cn.luorenmu.request.mihoyo.entiy.MihoyoResponse;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.11.19 15:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MihoyoDeviceFpResponse extends MihoyoResponse {
    private DeviceFp data;

    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    @Data
    public static class DeviceFp {
        private String deviceFp;
        private int code;
        private String msg;
    }
}
