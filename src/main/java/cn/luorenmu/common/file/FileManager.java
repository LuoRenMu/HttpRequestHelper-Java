package cn.luorenmu.common.file;

import cn.luorenmu.Main;
import cn.luorenmu.common.utils.LoggerUtil;
import cn.luorenmu.entiy.Config;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */


public class FileManager {

    public static final Config CONFIG = new Config();
    public static final String PATH = scanPath();
    public static final String FILE_ROOT_DIRECTORY = PATH.substring(0, PATH.lastIndexOf("target"));
    public static final List<String> FILE_NAME = new ArrayList<>();


    public static void fileGeneration(boolean remake) {
        String initFilePath = PATH + "/init";
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

    private static String scanPath() {
        String path = Objects.requireNonNull(Main.class.getClassLoader().getResource("cn/luorenmu")).getFile();
        if (path.contains("%")) {
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        }
        return path.substring(1, path.lastIndexOf("cn"));
    }

    public static void main(String[] args) {
        fileGeneration(false);
    }
}
