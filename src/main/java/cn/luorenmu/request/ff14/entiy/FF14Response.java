package cn.luorenmu.request.ff14.entiy;

import cn.luorenmu.common.utils.Notifications;
import lombok.Data;


/**
 * @author LoMu
 * Date 2023.12.19 17:50
 */
@Data
public class FF14Response {
    private int code;
    private String msg;

    /**
     * 用于判断请求是否成功(兜底)
     *
     * @return bool
     */
    public boolean isSuccess() {
        if (code != 10000) {
            Notifications.sendAllNotification("意外: 请求返回Code未达到预期", msg);
            return false;
        }
        return true;
    }

}
