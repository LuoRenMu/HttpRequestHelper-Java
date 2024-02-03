package cn.luorenmu.request.ff14.entiy;

import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.config.NotificationSetting;
import cn.luorenmu.entiy.RequestType;
import cn.luorenmu.entiy.RunStorage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author LoMu
 * Date 2023.12.19 17:50
 */
@Data
@Slf4j
public class FF14Response {
    private int code;
    private String msg;

    /**
     * 用于判断请求是否成功(兜底)
     *
     * @return bool
     */
    public boolean isSuccess() {
        NotificationSetting config = RunStorage.getConfig(NotificationSetting.class);
        List<NotificationSetting.NotificationResponseCode> list = config.getNotify().stream().filter(e -> e.getResponseType() == RequestType.FF14).toList();
        for (NotificationSetting.NotificationResponseCode notificationResponseCode : list) {
            if (code == notificationResponseCode.getCode()) {
                if (notificationResponseCode.isNotify()) {
                    Notifications.sendEmailNotify(notificationResponseCode.getResponseType().name(), notificationResponseCode.getMessage() + ": " + msg);
                }
                return notificationResponseCode.isSuccess();
            }
        }
        Notifications.sendEmailNotify("意外: 请求返回Code未达到预期", "错误代码: " + code + "          错误信息: " + msg);
        return false;
    }

}
