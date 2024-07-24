package cn.luorenmu.push.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.push.Push;
import cn.luorenmu.push.entiy.ServerChanResponse;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author LoMu
 * Date 2023.10.28 18:57
 */
@Slf4j
public class ServerChanPush implements Push {

    private final String url;

    public ServerChanPush(String key) {
        this.url = RunStorage.getConfig("setting.json").to(Setting.class).getGeneral().getServerChanUrl() + key + ".send?";
    }


    public boolean executeShortMessage(String title) {
        return executeLongMessage(title, title);
    }

    public boolean executeLongMessage(String title, String message) {
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
