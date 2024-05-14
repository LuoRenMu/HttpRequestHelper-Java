package cn.luorenmu.exception;

import cn.luorenmu.common.utils.Notifications;

/**
 * @author LoMu
 * Date 2024.02.06 14:39
 */
public class RequestHumanTakeoverException extends RuntimeException {
    public RequestHumanTakeoverException(String message) {
        super(message);
        Notifications.sendAllNotification("AutoHelper:RequestHumanTakeoverException", message);
        System.exit(0);
    }

    public RequestHumanTakeoverException(Throwable cause) {
        super(cause);
    }
}
