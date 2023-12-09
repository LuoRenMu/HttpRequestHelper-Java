package cn.luorenmu.notification;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Setting;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * @author LoMu
 * Date 2023.10.28 18:57
 */
@Slf4j
public class ServerChanNotification {
    private final static String URL = FileManager.getConfig(Setting.class).getGeneral().getServerChanUrl() + FileManager.getConfig(Setting.class).getAccounts().get(0).getNotification().getServerChanKey() + ".send?";


    public static void sendTitle(String title) {
        sendTitleAndMessage(title, "");
    }

    public static void sendTitleAndMessage(String title, String message) {
        if (title == null) {
            title = "标题意外丢失";
        }
        if (title.length() > 32) {
            log.warn("server chan 可接受标题超出上限 : {}", title.length());
        }

        HttpRequest post = HttpUtil.createPost(URL);
        post.form(Map.of("title", title, "desp", message));
        HttpResponse execute = post.execute();
        log.debug("server chan send : {}", execute.body());
    }

    public static void main(String[] args) {
        sendTitleAndMessage("test", "hello");
    }


}
