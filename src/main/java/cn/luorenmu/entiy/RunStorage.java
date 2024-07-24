package cn.luorenmu.entiy;

import cn.luorenmu.entiy.config.Setting;
import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 * @author LoMu
 * Date 2024.01.18 19:36
 */
public class RunStorage {
    public static final ThreadLocal<Setting.Account> accountThreadLocal = new ThreadLocal<>();
    public static Map<String, JSONObject> CONFIG_ENITYS;

    public static JSONObject getConfig(String tClass) {
        return CONFIG_ENITYS.get(tClass);
    }
}
