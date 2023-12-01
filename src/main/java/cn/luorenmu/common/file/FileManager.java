package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.entiy.Setting;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */


public class FileManager {

    public static final Setting CONFIG = new Setting();
    public static final String RESOURCES_PATH = scanProjectFilePath();
    public static final String FILE_PATH = scanFilePath();
    public static final String PACKAGE_PATH = "cn.luorenmu";
    public static final String[] FILES_NAME = {"request.json", "setting.json"};
    public static final Map<Class<?>, Object> CONFIG_ENITYS;

    static {
        fileGeneration(false);
        CONFIG_ENITYS = ReadWriteFile.initConfig();
        LoggerUtil.log.info("文件操作已完成");
    }

    private static String scanFilePath() {
        URL location = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = location.getPath().substring(0, location.getPath().lastIndexOf("/") + 1);
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        System.out.println("FILE_PATH: " + filePath);
        return filePath;
    }

    private static void scanProjectFileStream(String fileName, String outputPath) {
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

    private static String scanProjectFilePath() {
        URL resource = FileManager.class.getClassLoader().getResource("");
        if (resource != null) {
            String path = resource.getPath();
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);
            return path;
        }
        System.out.println("RESOURCES_PATH: is null");
        return "";
    }


    public static void fileGeneration(boolean remake) {
        for (String fileName : FILES_NAME) {
            String outputFilePath = FILE_PATH + fileName;
            File file = new File(outputFilePath);
            if (!file.exists() || remake) {
                scanProjectFileStream(fileName, outputFilePath);
            }
        }
        LoggerUtil.log.info("文件初始化完成");

    }


}
