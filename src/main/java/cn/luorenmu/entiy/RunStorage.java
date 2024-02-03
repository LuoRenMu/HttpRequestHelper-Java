package cn.luorenmu.entiy;

import cn.luorenmu.config.Setting;

import java.util.Map;

/**
 * @author LoMu
 * Date 2024.01.18 19:36
 */
public class RunStorage {
    public static final ThreadLocal<Setting.Account> accountThreadLocal = new ThreadLocal<>();
    public static Map<Class<?>, Object> CONFIG_ENITYS;

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> tClass) {
        return (T) CONFIG_ENITYS.get(tClass);
    }
}
