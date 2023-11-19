package cn.luorenmu.common.utils;

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
}
