package cn.luorenmu.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.task.entiy.Games;
import cn.luorenmu.task.entiy.SignInfoRespone;
import cn.luorenmu.task.entiy.SignRespone;
import cn.luorenmu.task.entiy.SignRewardInfo;
import cn.luorenmu.task.entiy.account.MihoyoUserGameInfoResponse;
import cn.luorenmu.task.entiy.account.MihoyoUserTicketResponse;
import cn.luorenmu.task.entiy.account.MihoyoUserTokenResponse;
import cn.luorenmu.task.entiy.account.SignInUser;
import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.06 17:13
 */
public class MihoyoAccountService {

    private final static String SIGN_INFO_URL = "https://api-takumi.mihoyo.com/event/luna/info?lang=zh-cn";
    private final static String SIGN_URL = "https://api-takumi.mihoyo.com/event/luna/sign";


    public static Map<String, String> setSignMiHoyoForm(String uid, Games game) {
        Map<String, String> map = new HashMap<>();
        map.put("act_id", game.getActId());
        map.put("region", game.getGameInfo().getRegion());
        map.put("uid", uid);
        map.put("lang", "zh-cn");
        return map;
    }

    public static Map<String, String> setCookie(String cookie) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        return headers;
    }

    public MihoyoUserTokenResponse getCookieAccountInfoBySToken(String cookie) {
        HttpRequest httpRequest = HttpRequest.get("https://passport-api.mihoyo.com/account/auth/api/getCookieAccountInfoBySToken");
        httpRequest.addHeaders(setCookie(cookie));
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), MihoyoUserTokenResponse.class);

    }

    public SignInfoRespone getSignInfoRespone(SignInUser signInUser) {
        HttpRequest httpRequest = HttpUtil.createGet(SIGN_INFO_URL + signInUser.getGames().getParam() + signInUser.getUidParam());
        Map<String, String> headers = setCookie(signInUser.getCookie());
        httpRequest.addHeaders(headers);
        return JSON.parseObject(httpRequest.execute().body(), SignInfoRespone.class);
    }

    public SignRewardInfo getSignRewardInfo(Games game) {
        HttpRequest httpRequest = HttpUtil.createGet("https://api-takumi.mihoyo.com/event/luna/home?lang=zh-cn&act_id=" + game.getActId());
        return JSON.parseObject(httpRequest.execute().body(), SignRewardInfo.class);
    }

    public String cookieToUid(String cookie) {

        return null;
    }

    public MihoyoUserTicketResponse getActionTicketBySToken(String mihoyoUid) {
        HttpRequest httpRequest = HttpRequest.get(String.format("https://api-takumi.miyoushe.com/auth/api/getActionTicketBySToken?uid=%s&action_type=game_role", mihoyoUid));
        String body = httpRequest.execute().body();
        return JSON.parseObject(body, MihoyoUserTicketResponse.class);
    }

    public MihoyoUserGameInfoResponse getUserGameRoles(String ticket, Games games) {
        HttpRequest httpRequest = HttpRequest.get(String.format("https://api-takumi.mihoyo.com/binding/api/getUserGameRoles?action_ticket=%s&game_biz=%s", ticket, games.getGameInfo().getGameBiz()));
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), MihoyoUserGameInfoResponse.class);

    }

    public SignRewardInfo.GameData.Award getToDaySignInfo(SignInUser signInUser, Games game) {
        SignRewardInfo signInRewardInfo = getSignRewardInfo(game);
        List<SignRewardInfo.GameData.Award> awards = signInRewardInfo.getData().getAwards();
        int SignInDay = getSignInfoRespone(signInUser).getData().getShortSignDay();
        return awards.get(SignInDay - 1);
    }

    public String signOperate(SignInUser user, Map<String, String> form) {
        HttpRequest post = HttpRequest.post(SIGN_URL);
        post.addHeaders(setCookie(user.getCookie()));
        post.formStr(form);
        String body = post.execute().body();
        SignRespone signRespone = JSON.parseObject(body, SignRespone.class);
        if (signRespone.getData().getGt().isEmpty()) {
            LoggerUtil.log.info(signRespone.toString());
            return "签到完成";
        }

        String gt = signRespone.getData().getGt();
        String challenge = signRespone.getData().getChallenge();


        return "今日星穹铁道签到需要验证码完成";
    }
}
