package cn.luorenmu.common.utils;


import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.notification.Notification;
import cn.luorenmu.notification.impl.EmailNotification;
import cn.luorenmu.notification.impl.ServerChanNotification;

/**
 * @author LoMu
 * Date 2023.12.19 15:09
 */
public class Notifications {

    public static void sendAllNotification(String title, String message) {
        Setting.AccountNotification user = RunningStorage.accountThreadLocal.get().getNotification();
        if (!user.getServerChanKey().isBlank()) {
            Notification notification = new ServerChanNotification(user.getServerChanKey());
            notification.sendLongNotification(title, message);
        }
        if (!user.getEmail().isBlank()) {
            Notification notification = new EmailNotification(user.getEmail());
            notification.sendLongNotification(title, message);
        }
    }

    public static void sendEmailNotify(String title, String message) {
        Setting.AccountNotification user = RunningStorage.accountThreadLocal.get().getNotification();
        if (!user.getEmail().isBlank()) {
            Notification notification = new EmailNotification(user.getEmail());
            notification.sendLongNotification(title, message);
        }
    }
}
