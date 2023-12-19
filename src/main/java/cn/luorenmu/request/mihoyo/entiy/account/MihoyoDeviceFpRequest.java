package cn.luorenmu.request.mihoyo.entiy.account;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

/**
 * @author LoMu
 * Date 2023.11.19 14:04
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class MihoyoDeviceFpRequest {
    private String deviceId;
    private String seedTime;
    private String platform;
    private String seedId;
    private String deviceFp;
    private String appName;
    private String extFields;
}
