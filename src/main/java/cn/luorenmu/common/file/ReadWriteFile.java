package cn.luorenmu.common.file;

import cn.luorenmu.common.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static cn.luorenmu.common.file.FileManager.FILES_NAME;
import static cn.luorenmu.common.file.FileManager.ROOT_PATH;

/**
 * @author LoMu
 * Date 2023.11.22 23:03
 */
@Slf4j
public class ReadWriteFile {

    public static Map<Class<?>, Object> initConfig(boolean remake) {
        Map<Class<?>, Object> config = new HashMap<>();
        for (String s : FILES_NAME) {
            try {
                String fileName = "." + StringUtils.snakeCaseToCamelCase(s.substring(0, s.lastIndexOf(".")));
                checkFileThenCreate(s, remake);
                String json = readRootFileJson(s);
                Class<?> aClass = Class.forName(FileManager.PACKAGE_SETTING_PATH + fileName);
                Object o = JSON.parseObject(json, aClass);
                config.put(aClass, o);
            } catch (ClassNotFoundException | StringIndexOutOfBoundsException e) {
                log.error("文件解析失败 \nfile -> " + s + " <- init error");
                throw new RuntimeException();
            }
        }
        log.info("文件解析已完成");
        return config;
    }


    public static String readRootFileJson(String fileName) {
        String path = ROOT_PATH + fileName;
        log.debug("read file path : {}", path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("\nfile -> " + fileName + " <- read error\t" + e.getMessage());
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
            log.error("\nfile -> " + fileName + " <-  path -> + outputPath output error.\t" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static void checkFileThenCreate(String fileName, boolean remake) {
        String outputFilePath = ROOT_PATH + fileName;
        File file = new File(outputFilePath);
        if (!file.exists() || remake) {
            getFileStreamThenCreate(fileName, outputFilePath);
        }
        log.info("文件 {} 初始化完成", fileName);
    }

    public static void createDirs(String fileName) {
        if (fileName.contains("/") || fileName.contains("\\")) {
            String f = fileName.replaceAll("\\\\", "/");
            int indexOf = f.lastIndexOf("/");
            String substring = f.substring(0, indexOf);
            File file = new File(rootPathFileName(substring));
            if (!file.exists()) {
                boolean ignored = file.mkdirs();
            }
        }
    }

    public static OutputStream createRootFile(String fileName) {
        createDirs(fileName);

        String path = rootPathFileName(fileName);

        File file = new File(path);
        if (!file.exists()) {
            try {
                boolean ignored = file.createNewFile();
            } catch (IOException e) {
                log.error("\nfile -> " + fileName + " <- create error\t" + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            log.error("\nio stream -> " + path + " <- file not found\t" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String rootPathFileName(String fileName) {
        return ROOT_PATH + fileName;
    }

    public static <T> void entiyWriteFile(String name, T t) {
        try (OutputStream outputStream = createRootFile(name)) {
            String jsonString = JSON.toJSONString(t);
            outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("\nio stream -> " + name + " <- write failed");
            throw new RuntimeException(e);
        }
    }

    public static boolean fileExists(String fileName) {
        return new File(rootPathFileName(fileName)).exists();
    }

    public static InputStream readRootFile(String fileName) throws FileNotFoundException {
        String path = ROOT_PATH + fileName;
        File file = new File(path);
        return new FileInputStream(file);
    }
}
