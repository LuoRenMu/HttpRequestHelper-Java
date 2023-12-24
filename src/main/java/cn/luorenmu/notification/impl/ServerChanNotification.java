package cn.luorenmu.notification.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.notification.Notification;
import cn.luorenmu.notification.entiy.ServerChanResponse;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author LoMu
 * Date 2023.10.28 18:57
 */
@Slf4j
public class ServerChanNotification implements Notification {

    private final String url;

    public ServerChanNotification(String key) {
        this.url = FileManager.getConfig(Setting.class).getGeneral().getServerChanUrl() + key + ".send?";
    }


    public boolean sendShortNotification(String title) {
        return sendLongNotification(title, title);
    }

    public boolean sendLongNotification(String title, String message) {
        log.info("serverChan: 发送通知: {} \n {},", title, message);
        if (title == null) {
            title = "标题意外丢失";
        }
        if (title.length() > 32) {
            log.warn("server chan 可接受标题超出上限 : {}", title.length());
        }

        HttpRequest post = HttpUtil.createPost(this.url);
        post.form(Map.of("title", title, "desp", message));
        HttpResponse execute = post.execute();
        log.debug("server chan send : {}", execute.body());
        ServerChanResponse serverChanResponse = JSON.parseObject(execute.body(), ServerChanResponse.class);
        return serverChanResponse.getCode() == 0;
    }



}
