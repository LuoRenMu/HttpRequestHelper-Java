package cn.luorenmu.config;

import cn.luorenmu.entiy.RequestType;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.List;

/**
 * @author LoMu
 * Date 2024.01.18 18:31
 */
@Data
public class NotificationSetting {
    private List<NotificationResponseCode> notify;

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class NotificationResponseCode {

        private RequestType responseType;
        private int code;
        private String message;
        private boolean notify = true;
        private boolean success;

    }
}
