package cn.luorenmu;


import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.task.MiHoYoSign;

/**
 * @author LoMu
 * Date 2023.10.28 15:30
 */
public class Main {

    public static void main(String[] args) {
        LoggerUtil.log.info("已经开始工作,每日早上10点将开始自动签到");
        MiHoYoSign miHoYoSingIn = new MiHoYoSign();
        miHoYoSingIn.signTimer();
    }
}