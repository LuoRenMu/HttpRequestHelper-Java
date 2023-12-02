package cn.luorenmu.common.utils;

import cn.luorenmu.entiy.Request;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LoMu
 * Date 2023.12.02 20:32
 */

//进一步封装Http请求

@Slf4j
public class HttpRequest {
    private static final String[] SUPPORT_METHOD = {"get", "post"};

    public static void get(Request.RequestDetailed requestDetailed) {
        String method = requestDetailed.getMethod();
        if (!isRequestMethod(method)) {
            log.error("not support request method : {}", requestDetailed);
            return;
        }

    }

    private static boolean isRequestMethod(String method) {
        for (String support : SUPPORT_METHOD) {
            if (support.equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }
}
