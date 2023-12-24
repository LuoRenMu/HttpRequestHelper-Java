package cn.luorenmu;


import cn.luorenmu.task.MiHoYoSign;
import cn.luorenmu.task.RequestTask;

/**
 * @author LoMu
 * Date 2023.10.28 15:30
 */
public class Main {
    public static void main(String[] args) {
        MiHoYoSign miHoYoSign = new MiHoYoSign();
        miHoYoSign.isRecentArticleTask();
        RequestTask requestTask = new RequestTask();
        requestTask.execute();
    }
}