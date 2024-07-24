package cn.luorenmu.request.ff14.entiy;

import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.entiy.config.NotificationSetting;
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
        NotificationSetting config = RunStorage.getConfig("notification_setting.json").to(NotificationSetting.class);
        List<NotificationSetting.NotificationResponseCode> list = config.getNotify().stream().toList();
        for (NotificationSetting.NotificationResponseCode notificationResponseCode : list) {
            if (code == notificationResponseCode.getCode()) {

                return notificationResponseCode.isSuccess();
            }
        }
        return false;
    }

}
