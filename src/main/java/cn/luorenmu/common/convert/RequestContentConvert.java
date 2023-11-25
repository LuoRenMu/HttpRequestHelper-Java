package cn.luorenmu.common.convert;

import cn.luorenmu.entiy.Request;

/**
 * @author LoMu
 * Date 2023.11.25 18:56
 */
public class RequestContentConvert {

    public static String requestToGet(Request.RequestContent requestContent) {
        if (requestContent.getMethod().equalsIgnoreCase("get")) {
            StringBuilder url = new StringBuilder(requestContent.getUrl());
            url.append("?");
            for (Request.RequestParam param : requestContent.getParams()) {
                url.append(param.getName());
                url.append("=");
                url.append(param.getContent());
                url.append("&");
            }
            return url.toString();
        }
        return null;
    }
}
