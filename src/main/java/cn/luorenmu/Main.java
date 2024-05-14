package cn.luorenmu;


import cn.luorenmu.common.file.ReadWriteFile;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.timer.TaskHandleCenter;
import lombok.extern.slf4j.Slf4j;

import static cn.luorenmu.common.annotation.impl.CurrentSettingImpl.scanSettingInject;

/**
 * @author LoMu
 * Date 2023.10.28 15:30
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        boolean b = false;
        if (args != null) {
            for (String arg : args) {
                if (arg.toUpperCase().startsWith("-initConfig=".toUpperCase())) {
                    b = Boolean.parseBoolean(arg.replace("-initConfig=", ""));

                }
            }
        }
        RunStorage.CONFIG_ENITYS = ReadWriteFile.initConfig(b);
        scanSettingInject(Main.class);
        TaskHandleCenter taskHandleCenter = new TaskHandleCenter();
        taskHandleCenter.execute();
    }
}