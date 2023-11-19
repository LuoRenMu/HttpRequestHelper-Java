package cn.luorenmu.task;

import cn.luorenmu.common.utils.ScanUtil;
import cn.luorenmu.entiy.Config;
import cn.luorenmu.mihoyo.MihoyoAccountRequest;
import cn.luorenmu.mihoyo.entiy.Games;
import cn.luorenmu.mihoyo.entiy.SignInfoRespone;
import cn.luorenmu.mihoyo.entiy.account.MihoyoUserTokenResponse;
import cn.luorenmu.mihoyo.entiy.account.SignInUser;
import cn.luorenmu.notification.ServerChanNotification;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static cn.luorenmu.mihoyo.MihoyoAccountRequest.setSignMiHoyoForm;


/**
 * @author LoMu
 * Date 2023.10.28 18:47
 */
public class MiHoYoSign {

    private final MihoyoAccountRequest mihoyoAccountService = new MihoyoAccountRequest();


    public void signTimerTask() {
        List<SignInUser> userList = new ArrayList<>();
        String cookieStr = ScanUtil.CONFIG.getCookie();
        Config.SToken sToken = ScanUtil.CONFIG.getSToken();
        MihoyoUserTokenResponse cookieAccountInfoBySToken = mihoyoAccountService.getCookieAccountInfoBySToken(sToken.getSTokenStr());
        System.out.println(cookieAccountInfoBySToken);
        MihoyoUserTokenResponse.UserTokenData data = cookieAccountInfoBySToken.getData();
        userList.add(new SignInUser().setCookie(cookieStr).setGames(Games.STAR_RAIL).setUid(data.getUid()));

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
                SignInfoRespone signInInfoRespone = mihoyoAccountService.getSignInfoRespone(user);
                if (signInInfoRespone.getData().isSign()) {
                    ServerChanNotification.sendMessageTitle("今天的签到已经完成");
                    return;
                }
                String message = mihoyoAccountService.signOperate(user, setSignMiHoyoForm(user.getUid(), Games.STAR_RAIL));
                ServerChanNotification.sendMessageTitle(message);

            }
        }, initailDelay, period, TimeUnit.MINUTES);
    }





}
