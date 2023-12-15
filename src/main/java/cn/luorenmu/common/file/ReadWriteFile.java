package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static cn.luorenmu.common.file.FileManager.FILES_NAME;
import static cn.luorenmu.common.file.FileManager.FILE_PATH;

/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
@Slf4j
public class ReadWriteFile {


    public static Map<Class<?>, Object> initConfig() {
        Map<Class<?>, Object> config = new HashMap<>();
        for (String s : FILES_NAME) {
            try {
                String fileName = "." + StringUtils.snakeCaseToCamelCase(s.substring(0, s.lastIndexOf(".")));
                String json = readRootFileJson(s);
                Class<?> aClass = Class.forName(FileManager.PACKAGE_SETTING_PATH + fileName);
                Object o = JSON.parseObject(json, aClass);
                config.put(aClass, o);
            } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {

                log.error("文件解析失败 \nfile -> " + s + " <- init failed");
                throw new RuntimeException(e);
            }
        }
        log.info("文件解析已完成");
        return config;
    }

    public static String readRootFileJson(String filename) {
        String path = FILE_PATH + filename;
        log.debug("read file path : {}", path);
        checkFileThenGeneration(filename, false);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }


    private static void getFileStreamThenCreate(String fileName, String outputPath) {
        InputStream resourceAsStream = FileManager.class.getResourceAsStream("/init/" + fileName);
        if (resourceAsStream == null) {
            return;
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputPath))) {
            byte[] bytes = new byte[1024];
            int len;
            while ((len = resourceAsStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, len);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void checkFileThenGeneration(String fileName, boolean remake) {
        String outputFilePath = FILE_PATH + fileName;
        File file = new File(outputFilePath);
        if (!file.exists() || remake) {
            getFileStreamThenCreate(fileName, outputFilePath);
        }
        log.info("文件 {} 初始化完成", fileName);
    }
}
