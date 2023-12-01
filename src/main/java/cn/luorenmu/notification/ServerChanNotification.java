package cn.luorenmu.notification;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.entiy.Setting;


/**
 * @author LoMu
 * Date 2023.10.28 18:57
 */
public class ServerChanNotification {
    private final static String URL = FileManager.getConfig(Setting.class).getServerChanUrl() + FileManager.getConfig(Setting.class).getServerChanKey() + ".send?";


    public static void sendMessageTitle(String title) {
        String url = URL + "title=" + title;
        HttpRequest get = HttpUtil.createGet(url);
        HttpResponse execute = get.execute();
        LoggerUtil.log.config(execute.toString());
    }


}
