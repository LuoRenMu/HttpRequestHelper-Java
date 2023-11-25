package cn.luorenmu.common.file;

import cn.luorenmu.Main;
import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.entiy.Setting;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */


public class FileManager {

    public static final Setting CONFIG = new Setting();
    public static final String RESOURCES_PATH;
    public static final String PROJECT_PATH;
    public static final String FILE_ROOT_DIRECTORY;
    public static final String PACKAGE_PATH = "cn.luorenmu";
    public static final List<String> FILE_NAME = new ArrayList<>();
    public static final Map<Class<?>, Object> CONFIG_ENITYS;

    static {
        String path = Objects.requireNonNull(Main.class.getClassLoader().getResource("cn/luorenmu/")).getFile();
        if (path.contains("%")) {
            PROJECT_PATH = URLDecoder.decode(path, StandardCharsets.UTF_8);
        } else {
            PROJECT_PATH = path;
        }
        System.out.println(PROJECT_PATH);
        System.out.println(PACKAGE_PATH);
        RESOURCES_PATH = PROJECT_PATH.substring(1, PROJECT_PATH.lastIndexOf("cn"));
        FILE_ROOT_DIRECTORY = RESOURCES_PATH.substring(0, RESOURCES_PATH.lastIndexOf("target"));
        fileGeneration(false);
        CONFIG_ENITYS = ReadWriteFile.initConfig();
    }


    public static void fileGeneration(boolean remake) {
        String initFilePath = RESOURCES_PATH + "init";
        File initFile = new File(initFilePath);
        File[] files1 = initFile.listFiles();
        if (files1 != null) {
            for (File f : files1) {
                String fileName = f.getName();
                String localfilePath = FILE_ROOT_DIRECTORY + fileName;
                FILE_NAME.add(fileName);
                File file = new File(localfilePath);
                if (!file.exists() || remake) {
                    try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localfilePath))
                         ; BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(f))
                    ) {
                        byte[] bytes = new byte[1024];
                        int len;
                        while ((len = bufferedInputStream.read(bytes)) != -1) {
                            bufferedOutputStream.write(bytes, 0, len);
                            bufferedOutputStream.flush();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            LoggerUtil.log.warning("文件初始化失败");
        }
        LoggerUtil.log.info("文件初始化完成");

    }


}
