package cn.luorenmu.common.convert;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.MatchData;
import cn.luorenmu.entiy.config.Request;
import com.alibaba.fastjson2.JSON;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author LoMu
 * Date 2023.11.25 18:56
 */
public class RequestContentConvert {

    public static HttpRequest requestToPost(Request.RequestDetailed requestDetailed) {
        HttpRequest post = httpRequest(cn.hutool.http.Method.POST);
        replaceAddData(requestDetailed, post);
        return post;
    }

    private static HttpRequest httpRequest(cn.hutool.http.Method method) {
        return HttpRequest.of("12312312").method(method);
    }

    private static void replaceAddData(Request.RequestDetailed requestDetailed, HttpRequest request) {
        if (requestDetailed.getParams() != null) {
            List<Request.RequestParam> params = new ArrayList<>(requestDetailed.getParams());
            replaceParams(params);
            request.setUrl(paramToUrl(requestDetailed.getUrl(), params));
        }
        if (requestDetailed.getHeaders() != null) {
            List<Request.RequestParam> headers = new ArrayList<>(requestDetailed.getHeaders());
            replaceParams(headers);
            Map<String, String> headers1 = toHeaders(headers);
            request.addHeaders(headers1);
        }
        if (requestDetailed.getBody() != null) {
            List<Request.RequestParam> bodys = new ArrayList<>(requestDetailed.getBody());
            request.body(bodyToJson(bodys));
        }


    }

    private static void replaceParams(List<Request.RequestParam> params) {
        for (Request.RequestParam param : params) {
            Optional<String> s = MatchData.scanReplaceFieldName(param.getContent());
            if (s.isEmpty()) {
                continue;
            }
            String content = s.get();
            String className = s.get();
            if (content.contains("()")) {
                className = className.substring(0, content.lastIndexOf("."));
            }

            try {
                Class<?> aClass = Class.forName("cn.luorenmu.common.utils." + className);
                Method method = aClass.getMethod(content.substring(s.get().lastIndexOf(".") + 1, content.lastIndexOf("(")));
                String invoke = (String) method.invoke(null);
                param.setContent(invoke);

            } catch (ClassNotFoundException ignored) {

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        HttpRequest httpRequest = requestToGet(FileManager.getConfig(Request.class).getMihoyo().getArticleCollect());
        HttpResponse execute = httpRequest.execute();
        System.out.println(httpRequest);
        System.out.println(execute.body());
    }

    public static HttpRequest requestToGet(Request.RequestDetailed requestDetailed) {
        HttpRequest httpRequest = HttpRequest.get(requestDetailed.getUrl());
        replaceAddData(requestDetailed, httpRequest);
        return httpRequest;
    }


    private static String bodyToJson(List<Request.RequestParam> requestBody) {
        Map<String, String> map = new HashMap<>();
        for (Request.RequestParam body : requestBody) {
            map.put(body.getName(), body.getContent());
        }
        return JSON.toJSONString(map);
    }

    private static Map<String, String> toHeaders(List<Request.RequestParam> requestHeaders) {
        Map<String, String> map = new HashMap<>();
        for (Request.RequestParam header : requestHeaders) {
            map.put(header.getName(), header.getContent());
        }
        return map;
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
