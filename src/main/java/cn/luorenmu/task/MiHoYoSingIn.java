package cn.luorenmu.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.notification.ServerChanNotification;
import cn.luorenmu.task.entiy.Games;
import cn.luorenmu.task.entiy.SingInUsers;
import cn.luorenmu.utils.LoggerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author LoMu
 * Date 2023.10.28 18:47
 */
public class MiHoYoSingIn {
    private final static String SIGN_IN_URL = "https://api-takumi.mihoyo.com/event/luna/info?lang=zh-cn";
    private final static ObjectMapper ob = new ObjectMapper();


    public void singIn() {
        List<SingInUsers> userList = new ArrayList<>();
        userList.add(new SingInUsers().setUid(101106135L).setGames(Games.STAR_RAIL).setEmail("luorenmu@qq.com").setCookie("uni_web_token=; cookie_token=9YYx0BphGmyQ8gpVRPMJDPzS7zYrkjkl4kUcd3JR; account_id=76646516; ltoken=YLvSGPDyiqahDLw1iDtd90l2wj00dMaEi51MyQ7G; ltuid=76646516; cookie_token_v2=v2_uUGuEDU3U1cSboPDHtymgPZ7nG2HS_91iliWf7UwdHMdccJCSbBW2PI5QyskjJRoTjnziypWTH7PySeU5E9ZYp1paa4P0Oa7UHrQWYW5Mb6UcNIF2Dj_Zhhx81adHlE=; account_mid_v2=0vys9gt43m_mhy; account_id_v2=76646516; ltoken_v2=v2_tE2f6NdNfkQmFjC6vWUsGBJgGWD7UF_4tnTZtIpxdrJTOEUKxTxmsB_x9luXKk7pN58lsq4EG9IGup-sp3zbT3gZbHnIwLCYMisNQVm98pQbz3jc44lJ0FtQYgcYAts=; ltmid_v2=0vys9gt43m_mhy; ltuid_v2=76646516"));
        ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(1);

        LocalDateTime now = LocalDateTime.now();
        //设置时间
        LocalDateTime time = now.withHour(10).withMinute(0).withSecond(0).withNano(0);
        //如果现在时间在指定日期之后则添加七天
        if (now.isAfter(time)) {
            time = time.plusDays(1);
        }
        //获取两个时间之间的差值
        long initailDelay = Duration.between(now, time).toMinutes();
        long period = 1000 * 60 * 60 * 24;

        threadPool.scheduleAtFixedRate(() -> {
            for (SingInUsers user : userList) {
                HttpRequest httpRequest = HttpUtil.createGet(SIGN_IN_URL + user.getGames().getDisplay() + user.getUidParam());
                Map<String, String> headers = setCookie(user.getCookie());
                httpRequest.addHeaders(headers);
                HttpResponse response = httpRequest.execute();
                String message = "StarRail签到已完成! ----" + response;
                LoggerUtil.log.info(message);
                LoggerUtil.log.info("本月已连续签到:" + response);
                ServerChanNotification.sendMessageTitle(message);
            }
        }, initailDelay, period, TimeUnit.MINUTES);
    }

    private static Map<String, String> setCookie(String cookie) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        return headers;
    }


}
