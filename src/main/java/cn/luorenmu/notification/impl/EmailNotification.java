package cn.luorenmu.notification.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.notification.Notification;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LoMu
 * Date 2023.12.17 13:41
 */
public class EmailNotification implements Notification {
    private static final MailAccount MAIL_ACCOUNT = FileManager.getConfig(Setting.class).getGeneral().getMail();
    Set<String> emails;


    public EmailNotification(String email) {
        emails = new HashSet<>();
        emails.add(email);
    }

    public EmailNotification(Set<String> emails) {
        this.emails = emails;
    }

    public static void main(String[] args) {

    }

    @Override
    public boolean sendShortNotification(String title) {
        return sendLongNotification(title, title);
    }

    @Override
    public boolean sendLongNotification(String title, String message) {
        try {
            MailUtil.send(MAIL_ACCOUNT, emails, null, null, title, message, null, true);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

}