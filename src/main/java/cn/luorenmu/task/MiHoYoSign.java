package cn.luorenmu.task;

import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.mihoyo.MihoyoAccountRequest;
import cn.luorenmu.mihoyo.MihoyoForumRequest;
import cn.luorenmu.mihoyo.entiy.Games;
import cn.luorenmu.mihoyo.entiy.SignInfoRespone;
import cn.luorenmu.mihoyo.entiy.account.MihoyoUserTokenResponse;
import cn.luorenmu.mihoyo.entiy.account.SignInUser;
import cn.luorenmu.notification.ServerChanNotification;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.luorenmu.mihoyo.MihoyoAccountRequest.setSignMiHoyoForm;


/**
 * @author LoMu
 * Date 2023.10.28 18:47
 */

@Slf4j
public class MiHoYoSign {
    private static ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE;

    public MiHoYoSign() {
        AtomicInteger atomicInteger = new AtomicInteger(FileManager.getConfig(Setting.class).getAccounts().size());
        SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(atomicInteger.intValue(), runnable -> new Thread(runnable, atomicInteger.decrementAndGet() + ""));
    }

    public void signTimerTask() {
        List<SignInUser> userList = new ArrayList<>();
        String cookieStr = FileManager.getConfig(Setting.class).getAccounts().get(0).getMihoyo().getCookie();
        Setting.SToken sToken = FileManager.getConfig(Setting.class).getAccounts().get(0).getMihoyo().getSToken();
        MihoyoUserTokenResponse cookieAccountInfoBySToken = MihoyoAccountRequest.getCookieAccountInfoBySToken(sToken.getSTokenStr());
        System.out.println(cookieAccountInfoBySToken);
        MihoyoUserTokenResponse.UserTokenData data = cookieAccountInfoBySToken.getData();
        userList.add(new SignInUser().setCookie(cookieStr).setGames(Games.STAR_RAIL).setUid(data.getUid()));


        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            for (SignInUser user : userList) {
                SignInfoRespone signInInfoRespone = MihoyoAccountRequest.getSignInfoRespone(user);
                if (signInInfoRespone.getData().isSign()) {
                    ServerChanNotification.sendTitle("今天的签到已经完成");
                    return;
                }
                String message = MihoyoAccountRequest.signOperate(user, setSignMiHoyoForm(user.getUid(), Games.STAR_RAIL));
                ServerChanNotification.sendTitle(message);
            }
        }, 0, 24, TimeUnit.HOURS);
    }

    public void isRecentArticleTask() {
        MihoyoForumRequest mihoyoForumRequest = new MihoyoForumRequest();
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            Setting.Account account = FileManager.getConfig(Setting.class).getAccounts().get(Integer.parseInt(Thread.currentThread().getName()));
            RunningStorage.accountThreadLocal.set(account);
            try {
                mihoyoForumRequest.isRecentArticle();
                log.info("版本兑换码监测已运行");
            } catch (Exception e) {
                log.error("发生错误 : {}", e.toString());
            }

        }, 0, 1, TimeUnit.HOURS);
    }


}
