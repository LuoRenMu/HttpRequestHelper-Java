package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.LoggerUtil;
import com.alibaba.fastjson2.JSON;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static cn.luorenmu.common.file.FileManager.FILES_NAME;
import static cn.luorenmu.common.file.FileManager.FILE_PATH;

/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
public class ReadWriteFile {


    public static Map<Class<?>, Object> initConfig() {
        Map<Class<?>, Object> config = new HashMap<>();
        for (String s : FILES_NAME) {
            try {
                String fileName = String.valueOf(s.charAt(0)).toUpperCase() + s.substring(0, s.lastIndexOf(".")).substring(1);
                String json = readRootFileJson(s);
                String packagePath = FileManager.PACKAGE_PATH + ".entiy.";
                Class<?> aClass = Class.forName(packagePath + fileName);
                Object o = JSON.parseObject(json, aClass);
                config.put(aClass, o);
            } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {
                LoggerUtil.log.warning("文件解析失败 \nfile -> " + s + " <- init failed");
                throw new RuntimeException(e);
            }
        }
        LoggerUtil.log.info("文件解析已完成");
        return config;
    }

    public static String readRootFileJson(String filename) {
        String path = FILE_PATH + filename;
        if (!isExists(path)) {
            fileGeneration(false);
        }
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

    private static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }


    private static void getFileStreamThenCreate(String fileName, String outputPath) {
        InputStream resourceAsStream = FileManager.class.getResourceAsStream("/init/" + fileName);
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputPath))) {
            byte[] bytes = new byte[1024];
            int len;
            while (true) {
                assert resourceAsStream != null;
                if ((len = resourceAsStream.read(bytes)) == -1) break;
                bufferedOutputStream.write(bytes, 0, len);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void fileGeneration(boolean remake) {
        for (String fileName : FILES_NAME) {
            String outputFilePath = FILE_PATH + fileName;
            File file = new File(outputFilePath);
            if (!file.exists() || remake) {
                getFileStreamThenCreate(fileName, outputFilePath);
            }
        }
        LoggerUtil.log.info("文件初始化完成");

    }
}
