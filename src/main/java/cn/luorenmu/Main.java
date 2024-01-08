package cn.luorenmu;


import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.file.ReadWriteFile;
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
        FileManager.CONFIG_ENITYS = ReadWriteFile.initConfig(true);
        log.info("配置已被加载: {}", FileManager.CONFIG_ENITYS);
        MiHoYoSign miHoYoSign = new MiHoYoSign();
        miHoYoSign.isRecentArticleTask();
        RequestTask requestTask = new RequestTask();
        requestTask.execute();
    }
}