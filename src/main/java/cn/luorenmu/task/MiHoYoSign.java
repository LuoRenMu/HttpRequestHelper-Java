package cn.luorenmu.task;

import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.Setting;
import cn.luorenmu.mihoyo.MihoyoAccountRequest;
import cn.luorenmu.mihoyo.entiy.Games;
import cn.luorenmu.mihoyo.entiy.SignInfoRespone;
import cn.luorenmu.mihoyo.entiy.account.MihoyoUserTokenResponse;
import cn.luorenmu.mihoyo.entiy.account.SignInUser;
import cn.luorenmu.notification.ServerChanNotification;

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

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);


    public void signTimerTask() {
        List<SignInUser> userList = new ArrayList<>();
        String cookieStr = FileManager.CONFIG.getCookie();
        Setting.SToken sToken = FileManager.CONFIG.getSToken();
        MihoyoUserTokenResponse cookieAccountInfoBySToken = MihoyoAccountRequest.getCookieAccountInfoBySToken(sToken.getSTokenStr());
        System.out.println(cookieAccountInfoBySToken);
        MihoyoUserTokenResponse.UserTokenData data = cookieAccountInfoBySToken.getData();
        userList.add(new SignInUser().setCookie(cookieStr).setGames(Games.STAR_RAIL).setUid(data.getUid()));


        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            for (SignInUser user : userList) {
                SignInfoRespone signInInfoRespone = MihoyoAccountRequest.getSignInfoRespone(user);
                if (signInInfoRespone.getData().isSign()) {
                    ServerChanNotification.sendMessageTitle("今天的签到已经完成");
                    return;
                }
                String message = MihoyoAccountRequest.signOperate(user, setSignMiHoyoForm(user.getUid(), Games.STAR_RAIL));
                ServerChanNotification.sendMessageTitle(message);
            }
        }, 0, 24, TimeUnit.HOURS);
    }





}
