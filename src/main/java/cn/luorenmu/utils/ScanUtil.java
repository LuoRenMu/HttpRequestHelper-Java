package cn.luorenmu.utils;

import cn.luorenmu.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * @author LoMu
 * Date 2023.10.28 19:29
 */
public class ScanUtil {
    public static final Properties prop = new Properties();

    static {
        String filePath = Objects.requireNonNull(Main.class.getClassLoader().getResource("cn/luorenmu")).getFile();
        String path = filePath.replaceAll("%20", " ");
        path = path.substring(1, path.lastIndexOf("cn"));
        try {
            prop.load(Files.newInputStream(Paths.get(path + "config.properties")));
        } catch (IOException e) {
            LoggerUtil.log.warning(e.toString());
        }
    }
}
