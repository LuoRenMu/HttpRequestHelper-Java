package cn.luorenmu.common.file;


import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.06 19:12
 */


public class FileManager {

    protected static final String FILE_PATH = scanFilePath();
    protected static final String PACKAGE_PATH = "cn.luorenmu";
    protected static final String[] FILES_NAME = {"request.json", "setting.json"};
    private static final Map<Class<?>, Object> CONFIG_ENITYS = ReadWriteFile.initConfig();


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
