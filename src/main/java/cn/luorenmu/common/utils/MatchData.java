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
public class MatchData {
    public static Optional<String> scanReplaceFieldName(String s) {
        Pattern pattern = Pattern.compile("(?<=^\\$\\{)(.*)(?=})");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }

    public static Optional<String> matcherStr(String str, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }


    public static Optional<String> redeemCodes(String content) {
        String pattern = "((?<=<p>)[A-Z0-9]{12}(?=</p>))+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String group = matcher.group();
            sb.append(group).append("|");
        }
        if (sb.isEmpty()) {
            return Optional.empty();
        } else {
            sb.delete(sb.length() - 1, sb.length());
        }
        return Optional.of(sb.toString());
    }
}

