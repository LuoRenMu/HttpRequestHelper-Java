package cn.luorenmu.notification;

/**
 * @author LoMu
 * Date 2023.12.17 13:38
 */
public interface Notification {
    boolean sendShortNotification(String title);

    boolean sendLongNotification(String title, String message);
}
