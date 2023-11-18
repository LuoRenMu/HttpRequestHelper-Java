package cn.luorenmu.common.utils;

import cn.luorenmu.Main;
import cn.luorenmu.common.auto.Automatic;
import cn.luorenmu.entiy.Config;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/**
 * @author LoMu
 * Date 2023.10.28 19:29
 */
public class ScanUtil {
    public static final Config CONFIG = new Config();
    public static final String PATH;

    static {
        Properties prop = new Properties();
        String path = Objects.requireNonNull(Main.class.getClassLoader().getResource("cn/luorenmu")).getFile();
        if (path.contains("%")) {
            path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        }
        PATH = path.substring(1, path.lastIndexOf("cn"));
        try {
            prop.load(Files.newInputStream(Paths.get(PATH + "config.properties")));
        } catch (IOException e) {
            LoggerUtil.log.warning(e.toString());
            throw new RuntimeException("CONFIG load failed  ):");
        }

        Set<Object> keySet = prop.keySet();
        for (Object o : keySet) {
            Automatic.AutoSetFieldEquals(CONFIG, Config.class, o.toString(), String.class, prop.get(o));
        }

    }

}
