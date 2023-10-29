package cn.luorenmu.task;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.notification.ServerChanNotification;
import cn.luorenmu.task.entiy.Games;
import cn.luorenmu.task.entiy.SignInInfoRespone;
import cn.luorenmu.task.entiy.SignInRewardInfo;
import cn.luorenmu.task.entiy.SignInUser;
import com.alibaba.fastjson2.JSON;

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
public class MiHoYoSignIn {
    private final static String SIGN_IN_URL = "https://api-takumi.mihoyo.com/event/luna/info?lang=zh-cn";


    public static Map<String, String> setMiHoyoForm() {

        return null;
    }

    private static Map<String, String> setCookie(String cookie) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        return headers;
    }

    private SignInInfoRespone getSignInInfoRespone(SignInUser signInUser) {
        HttpRequest httpRequest = HttpUtil.createGet(SIGN_IN_URL + signInUser.getGames().getParam() + signInUser.getUidParam());
        Map<String, String> headers = setCookie(signInUser.getCookie());
        httpRequest.addHeaders(headers);
        return JSON.parseObject(httpRequest.execute().body(), SignInInfoRespone.class);
    }

    private SignInRewardInfo getSignInRewardInfo(String gameId) {
        HttpRequest httpRequest = HttpUtil.createGet("https://api-takumi.mihoyo.com/event/luna/home?lang=zh-cn&act_id=e202304121516551");
        return JSON.parseObject(httpRequest.execute().body(), SignInRewardInfo.class);
    }

    public static void main(String[] args) {
        MiHoYoSignIn miHoYoSignIn = new MiHoYoSignIn();

    }

    public void singIn() {
        List<SignInUser> userList = new ArrayList<>();
        userList.add(new SignInUser().setUid(101106135L).setGames(Games.STAR_RAIL).setEmail("luorenmu@qq.com").setCookie("uni_web_token=; cookie_token=9YYx0BphGmyQ8gpVRPMJDPzS7zYrkjkl4kUcd3JR; account_id=76646516; ltoken=YLvSGPDyiqahDLw1iDtd90l2wj00dMaEi51MyQ7G; ltuid=76646516; cookie_token_v2=v2_uUGuEDU3U1cSboPDHtymgPZ7nG2HS_91iliWf7UwdHMdccJCSbBW2PI5QyskjJRoTjnziypWTH7PySeU5E9ZYp1paa4P0Oa7UHrQWYW5Mb6UcNIF2Dj_Zhhx81adHlE=; account_mid_v2=0vys9gt43m_mhy; account_id_v2=76646516; ltoken_v2=v2_tE2f6NdNfkQmFjC6vWUsGBJgGWD7UF_4tnTZtIpxdrJTOEUKxTxmsB_x9luXKk7pN58lsq4EG9IGup-sp3zbT3gZbHnIwLCYMisNQVm98pQbz3jc44lJ0FtQYgcYAts=; ltmid_v2=0vys9gt43m_mhy; ltuid_v2=76646516"));
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
            for (SignInUser user : userList) {
                SignInInfoRespone signInInfoRespone = getSignInInfoRespone(user);
                if (signInInfoRespone.getData().isSign()) {
                    ServerChanNotification.sendMessageTitle("今天的签到已经完成");
                    return;
                }


            }
        }, initailDelay, period, TimeUnit.MINUTES);
    }

    private SignInRewardInfo.GameData.Award getToDaySignInInfo(SignInUser signInUser, String gameId) {
        SignInRewardInfo signInRewardInfo = getSignInRewardInfo(gameId);
        List<SignInRewardInfo.GameData.Award> awards = signInRewardInfo.getData().getAwards();
        int SignInDay = getSignInInfoRespone(signInUser).getData().getShortSignDay();
        return awards.get(SignInDay - 1);
    }

    private String signInOperate(SignInUser user, Map<String, String> params) {
        HttpRequest post = HttpRequest.post("https://api-takumi.mihoyo.com/event/luna/sign");
        post.addHeaders(setCookie(user.getCookie()));
        post.formStr(params);
        String body = post.execute().body();

        return null;
    }

}
