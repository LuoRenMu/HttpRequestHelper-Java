package cn.luorenmu.common.utils;

import cn.luorenmu.Main;
import cn.luorenmu.common.auto.Automatic;
import cn.luorenmu.entiy.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    public static final Config config = new Config();

    static {
        Properties prop = new Properties();
        String path = Objects.requireNonNull(Main.class.getClassLoader().getResource("cn/luorenmu")).getFile();
        if (path.contains("%")) {
            try {
                path = URLDecoder.decode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        path = path.substring(1, path.lastIndexOf("cn"));
        try {
            prop.load(Files.newInputStream(Paths.get(path + "config.properties")));
        } catch (IOException e) {
            LoggerUtil.log.warning(e.toString());
            throw new RuntimeException("config load failed  ):");
        }

        Set<Object> keySet = prop.keySet();
        for (Object o : keySet) {
            Automatic.AutoSetFieldEquals(config, Config.class, o.toString(), String.class, prop.get(o));
        }

    }

}
