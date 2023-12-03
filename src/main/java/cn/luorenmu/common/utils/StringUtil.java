package cn.luorenmu.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

/**
 * @author LoMu
 * Date 2023.11.19 11:41
 */
public class StringUtil {
    /**
     * 获取随机字符串用于DS算法中
     *
     * @return String
     */
    public static String getRandomStr(int num) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            String CONSTANTS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int number = random.nextInt(CONSTANTS.length());
            char charAt = CONSTANTS.charAt(number);
            sb.append(charAt);
        }
        return sb.toString();
    }

    public static String firstCharacterUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = StringUtil.getRandomStr(6);
        return createDS("uTUzziiV9FazyGA7XgVIk287ZczinFRV", i, r);
    }


    /**
     * 创建DS算法
     *
     * @param n (<a href="https://github.com/UIGF-org/mihoyo-api-collect/issues/1">salt</a>)
     * @param i time
     * @param r random String
     * @return String
     */
    private static String createDS(String n, String i, String r) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r);
        return String.format("%s,%s,%s", i, r, c);
    }

}
