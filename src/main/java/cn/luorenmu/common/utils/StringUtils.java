package cn.luorenmu.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LoMu
 * Date 2023.11.19 11:41
 */
public class StringUtils {
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

    public static String snakeCaseToCamelCase(String string) {
        if (!string.contains("_")) {
            return firstCharacterUpperCase(string);
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] s = string.split("_");
        for (String s1 : s) {
            stringBuilder.append(firstCharacterUpperCase(s1));
        }
        return stringBuilder.toString();
    }

    public static String camelCaseToSnakeCase(String substring) {
        int[] ints = upperCaseIndex(substring);
        if (ints.length == 0) {
            return substring;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int lastIndex = 0;
        for (int anInt : ints) {
            if (substring.length() - 1 == anInt) {
                break;
            }
            String spiltStr = substring.substring(0, anInt);
            stringBuilder.append(spiltStr);
            stringBuilder.append("_");
            lastIndex = anInt;
        }
        stringBuilder.append(substring.substring(lastIndex));
        return stringBuilder.toString();
    }

    public static int[] upperCaseIndex(String str) {
        char[] charArray = str.toCharArray();
        List<Integer> integers = new ArrayList<>();
        for (int i = 1; i < charArray.length; i++) {
            int ascii = charArray[i];
            if (ascii >= 65 && ascii <= 90) {
                integers.add(i);
            }
        }
        return Arrays.stream(integers.toArray(new Integer[0])).mapToInt(Integer::intValue).toArray();
    }

    /**
     * 第一个字符大写
     *
     * @param str 字符
     * @return String
     */

    public static String firstCharacterUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    public static String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = StringUtils.getRandomStr(6);
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
