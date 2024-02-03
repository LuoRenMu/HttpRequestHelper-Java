package cn.luorenmu;


import cn.luorenmu.common.file.ReadWriteFile;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.task.MiHoYoSign;
import cn.luorenmu.task.RequestTask;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LoMu
 * Date 2023.10.28 15:30
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        RunStorage.CONFIG_ENITYS = ReadWriteFile.initConfig(true);
        MiHoYoSign miHoYoSign = new MiHoYoSign();
        miHoYoSign.isRecentArticleTask();
        RequestTask requestTask = new RequestTask();
        requestTask.execute();

    }
}