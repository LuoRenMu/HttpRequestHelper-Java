package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.LoggerUtil;
import com.alibaba.fastjson2.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
public class ReadWriteFile {


    public static Map<Class<?>, Object> initConfig() {
        Map<Class<?>, Object> config = new HashMap<>();
        for (String s : FileManager.FILES_NAME) {
            try {
                String fileName = String.valueOf(s.charAt(0)).toUpperCase() + s.substring(0, s.lastIndexOf(".")).substring(1);
                String json = readRootFileJson(s);
                String packagePath = FileManager.PACKAGE_PATH + ".entiy.";

                Class<?> aClass = Class.forName(packagePath + fileName);
                Object o = JSON.parseObject(json, aClass);
                config.put(aClass, o);
            } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {
                LoggerUtil.log.warning("file -> " + s + " <- init failed");
                throw new RuntimeException(e);
            }
        }
        LoggerUtil.log.info("文件解析已完成");
        return config;
    }

    public static String readRootFileJson(String filename) {
        String fileRootDirectory = FileManager.FILE_PATH;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileRootDirectory + filename))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

}
