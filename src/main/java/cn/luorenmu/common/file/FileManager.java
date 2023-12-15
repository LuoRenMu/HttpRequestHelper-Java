package cn.luorenmu.common.file;


import cn.hutool.core.lang.ClassScanner;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.entiy.config.Setting;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */

@Slf4j
public class FileManager {

    protected static final String FILE_PATH = scanFilePath();
    protected static final String PACKAGE_SETTING_PATH = Setting.class.getPackageName();
    protected static final Set<String> FILES_NAME = new HashSet<>();
    private static final Map<Class<?>, Object> CONFIG_ENITYS;

    static {
        setFilesName();
        CONFIG_ENITYS = ReadWriteFile.initConfig();
        log.info("配置已被加载: {}", CONFIG_ENITYS);
    }

    public static void setFilesName() {
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


    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> tClass) {
        return (T) CONFIG_ENITYS.get(tClass);
    }


}
