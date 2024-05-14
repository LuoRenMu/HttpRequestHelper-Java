package cn.luorenmu.common.file;


import cn.hutool.core.lang.ClassScanner;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.config.Setting;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */

@Slf4j
public class FileManager {
    public static final String ROOT_PATH = scanFilePath();
    protected static final String PACKAGE_SETTING_PATH = Setting.class.getPackageName();
    protected static final Set<String> FILES_NAME = new HashSet<>();



    static {
        initFilesName();
    }

    public static void initFilesName() {
        ClassScanner scanner = new ClassScanner(PACKAGE_SETTING_PATH);
        Set<Class<?>> classes = scanner.scan();
        for (Class<?> aClass : classes) {
            String name = aClass.getTypeName();
            String substring = name.substring(name.lastIndexOf(".") + 1);
            if (substring.contains("$")) {
                continue;
            }
            substring = StringUtils.camelCaseToSnakeCase(substring);
            FILES_NAME.add(substring.toLowerCase() + ".json");
        }
    }


    private static String scanFilePath() {
        URL location = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = location.getPath().substring(0, location.getPath().lastIndexOf("/") + 1);
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        return filePath;
    }


}
