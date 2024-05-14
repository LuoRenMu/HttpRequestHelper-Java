package cn.luorenmu.exception;

import cn.luorenmu.common.utils.Notifications;

/**
 * @author LoMu
 * Date 2024.02.06 14:46
 */
public class CurrentTaskCannotContinueException extends RuntimeException {
    public CurrentTaskCannotContinueException(String message) {
        super(message);
        Notifications.sendAllNotification("AutoHelper:CurrentTaskCannotContinueException", message);
    }
}
