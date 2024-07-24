package cn.luorenmu.file;


import cn.luorenmu.entiy.RunStorage;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static cn.luorenmu.file.ReadWriteFile.*;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */

@Slf4j
public class InitializeFile {

    private static final String[] INIT_FILES = {
            "setting.json", "notification_setting.json", "request/ff14_request.json", "request/mihoyo_request.json"
    };

    private static final String[] INIT_DIRS = {
            "request"
    };


    private static final String[] CURRENT_DIR = {
            "cache/"
    };

    public static Map<String, JSONObject> initConfig(Class<?> mainClazz, boolean remake) {
        ReadWriteFile.CURRENT_PATH = ReadWriteFile.scanFilePath(mainClazz);
        Map<String, JSONObject> config = new HashMap<>();

        for (String fileName : INIT_FILES) {
            checkFileThenCreate(fileName, remake);
            String json = readCurrentFileJson(fileName);
            JSONObject o = JSON.parseObject(json);
            config.put(fileName, o);
        }
        for (String dir : INIT_DIRS) {
            File[] files = currentDirs(dir);
            JSONObject jsonObject = readFiles(files);
            config.put(dir, jsonObject);
        }

        for (String s : CURRENT_DIR) {
            createCurrentDirs(s);
        }
        log.info("文件解析已完成");
        return config;
    }


    public static void run(Class<?> clazz, boolean remake) {
        RunStorage.CONFIG_ENITYS = initConfig(clazz, remake);
    }

    public static void main(String[] args) {
        run(InitializeFile.class, true);
    }
}
