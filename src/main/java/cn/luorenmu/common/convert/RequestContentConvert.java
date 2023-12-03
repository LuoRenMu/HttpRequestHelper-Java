package cn.luorenmu.common.convert;

import cn.hutool.http.HttpRequest;
import cn.luorenmu.entiy.Request;
import com.alibaba.fastjson2.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.25 18:56
 */
public class RequestContentConvert {


    public static HttpRequest requestToPost(Request.RequestDetailed requestDetailed) {
        String url = paramToUrl(requestDetailed.getUrl(), requestDetailed.getParams());
        HttpRequest post = HttpRequest.post(url);
        String json = bodyToJson(requestDetailed.getBody());
        post.body(json);
        return post;
    }

    public static HttpRequest requestToGet(Request.RequestDetailed requestDetailed) {
        return HttpRequest.get(paramToUrl(requestDetailed.getUrl(), requestDetailed.getParams()));
    }

    private static String bodyToJson(List<Request.RequestParam> requestBody) {
        Map<String, String> map = new HashMap<>();
        for (Request.RequestParam body : requestBody) {
            map.put(body.getName(), body.getContent());
        }
        return JSON.toJSONString(map);
    }

    private static String paramToUrl(String url, List<Request.RequestParam> requestParams) {
        if (requestParams == null || requestParams.isEmpty()) {
            return url;
        }
        StringBuilder newUrl = new StringBuilder(url);
        newUrl.append("?");
        for (Request.RequestParam param : requestParams) {
            newUrl.append(param.getName());
            newUrl.append("=");
            newUrl.append(param.getContent());
            newUrl.append("&");
        }
        return newUrl.toString();
    }
}
