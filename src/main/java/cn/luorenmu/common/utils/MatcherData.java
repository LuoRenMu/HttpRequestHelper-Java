package cn.luorenmu.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LoMu
 * Date 2023.12.07 21:20
 */

@Slf4j
public class MatcherData {
    public static Optional<String> scanReplaceFieldName(String s) {
        String pattern = "(?<=\\$\\{)(.*)(?=})";
        return matcherStr(s, pattern, 1, null);
    }


    public static Optional<String> matcherStr(String str, String patternStr, int limit, String separateStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        int num = 0;
        while (matcher.find()) {
            String group = matcher.group();
            sb.append(group);
            if (separateStr != null) {
                sb.append(separateStr);
            }
            if (limit != 0) {
                num++;
                if (limit == num) {
                    return Optional.of(sb.toString());
                }
            }
        }
        if (sb.isEmpty()) {
            return Optional.empty();
        } else if (separateStr != null && !separateStr.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return Optional.of(sb.toString());
    }


    public static Optional<String> redeemCodes(String content) {
        String pattern = "((?<=<p>)[A-Z0-9]{12}(?=</p>))+";
        return matcherStr(content, pattern, 3, ",");
    }

}

