package cn.luorenmu.push.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.push.Push;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * @author LoMu
 * Date 2023.12.17 13:41
 */
@Slf4j
public class EmailPush implements Push {
    private static final MailAccount MAIL_ACCOUNT = RunStorage.getConfig("setting.json").to(Setting.class).getGeneral().getMail();
    Set<String> emails;


    public EmailPush(String email) {
        emails = new HashSet<>();
        emails.add(email);
    }

    public EmailPush(Set<String> emails) {
        this.emails = emails;
    }


    @Override
    public boolean executeShortMessage(String title) {
        return executeLongMessage(title, title);
    }

    @Override
    public boolean executeLongMessage(String title, String message) {
        try {
            log.info("email: 发送通知 -> {} : {} \n {},", emails, title, message);
            MailUtil.send(MAIL_ACCOUNT, emails, null, null, title, message, null, true);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

}
