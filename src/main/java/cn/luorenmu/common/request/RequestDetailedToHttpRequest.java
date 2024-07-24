package cn.luorenmu.common.request;

import cn.hutool.http.HttpRequest;
import cn.luorenmu.common.utils.MatcherData;
import cn.luorenmu.entiy.Request;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.exception.MisconfigurationException;
import com.alibaba.fastjson2.JSON;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author LoMu
 * Date 2023.11.25 18:56
 */

@Accessors(chain = true)
@Slf4j
public class RequestDetailedToHttpRequest {
    private final HttpRequest HTTP_REQUEST = HttpRequest.of("**********");
    @Setter
    private Request.RequestDetailed requestDetailed;
    private final List<String> args;

    {
        HTTP_REQUEST.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0");
    }

    public RequestDetailedToHttpRequest(Request.RequestDetailed requestDetailed, String... args) {
        this.requestDetailed = requestDetailed;
        this.args = List.of(args);
        replaceAddData();
    }

    private RequestDetailedToHttpRequest() {
        args = new ArrayList<>();
    }

    /**
     * TODO
     *
     * @return TEST OBJECT
     */
    public static RequestDetailedToHttpRequest buildTestObject() {
        return new RequestDetailedToHttpRequest();
    }

    public HttpRequest requestToPost() {
        HTTP_REQUEST.method(cn.hutool.http.Method.POST);
        return HTTP_REQUEST;
    }

    public HttpRequest requestToGet() {
        HTTP_REQUEST.method(cn.hutool.http.Method.GET);
        return HTTP_REQUEST;
    }

    private void replaceAddData() {
        if (requestDetailed.getParams() != null) {
            List<Request.RequestParam> params = replaceParamsReturnNew(requestDetailed.getParams());
            HTTP_REQUEST.setUrl(paramToUrl(requestDetailed.getUrl(), params));
        } else {
            HTTP_REQUEST.setUrl(paramToUrl(requestDetailed.getUrl(), null));
        }
        if (requestDetailed.getHeaders() != null) {
            List<Request.RequestParam> headers = replaceParamsReturnNew(requestDetailed.getHeaders());
            HTTP_REQUEST.addHeaders(toHeaders(headers));
        }
        if (requestDetailed.getBody() != null) {
            List<Request.RequestParam> bodys = replaceParamsReturnNew(requestDetailed.getBody());
            HTTP_REQUEST.body(RequestParamToJsonStr(bodys));
        }


    }

    /**
     * 如果content是使用${}将进行数据替换 在不改变原有数据的情况下 创建的新的对象返回
     *
     * @param params RequestParam
     * @return RequestParam
     */
    private List<Request.RequestParam> replaceParamsReturnNew(List<Request.RequestParam> params) {
        List<Request.RequestParam> newParams = new ArrayList<>(params);
        for (Request.RequestParam param : params) {
            Optional<String> s = MatcherData.scanMethodFieldName(param.getContent());

            if (s.isEmpty()) {
                newParams.add(param);
                continue;
            }
            String content = s.get();
            if (content.equalsIgnoreCase("cookie")) {

                // TODO 根据请求类型选择cookie
                param.setContent(RunStorage.accountThreadLocal.get().getFf14().getCookie());

            } else if (content.equalsIgnoreCase("customize")) {
                //TODO
                param.setContent(args.get(0));
            } else {
                String classMethodInvoke = findClassMethodInvoke(content);
                param.setContent(param.getContent().replace(String.format("${%s}", content), classMethodInvoke));
            }

        }
        return newParams;
    }

    private String RequestParamToJsonStr(List<Request.RequestParam> requestBody) {
        Map<String, String> map = new HashMap<>();
        for (Request.RequestParam body : requestBody) {
            map.put(body.getName(), body.getContent());
        }
        return JSON.toJSONString(map);
    }

    private Map<String, String> toHeaders(List<Request.RequestParam> requestHeaders) {
        Map<String, String> map = new HashMap<>();
        for (Request.RequestParam header : requestHeaders) {
            map.put(header.getName(), header.getContent());
        }
        return map;
    }

    private String findClassMethodInvoke(String methodStr) {
        String[] split = methodStr.split(":");
        if (split.length < 2) {
            log.error("不被允许的配置 {}  requsetDetailed : {}", methodStr, requestDetailed.toString());
            throw new MisconfigurationException("不被允许的配置" + methodStr);
        }
        try {
            Class<?> aClass = Class.forName("cn.luorenmu.common.utils." + split[0]);
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                // TODO: 形参匹配
                if (method.getName().equalsIgnoreCase(split[1]) && method.getParameterCount() == split.length - 2) {

                    if (split.length == 2) {
                        return (String) method.invoke(null);
                    } else {
                        String[] strings = Arrays.copyOfRange(split, 2, split.length);
                        return (String) method.invoke(null, (Object[]) strings);
                    }
                }
            }
            throw new NoSuchMethodException();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("Http request failed requestDetailed : {} ", requestDetailed.toString());
            throw new RuntimeException(e);
        }
    }

    private String paramToUrl(String url, List<Request.RequestParam> requestParams) {
        Optional<String> s = MatcherData.scanMethodFieldName(url);
        if (s.isPresent()) {
            String methodInvoke = findClassMethodInvoke(s.get());
            url = url.replace(String.format("${%s}", s.get()), methodInvoke);
        }

        if (requestParams == null || requestParams.isEmpty()) {
            return url;
        }

        StringBuilder newUrl = new StringBuilder(url);
        newUrl.append("?");
        for (Request.RequestParam param : requestParams) {
            newUrl.append(param.getName());
            newUrl.append("=");
            Optional<String> contentOptional = MatcherData.scanMethodFieldName(param.getContent());
            String content = param.getContent();
            if (contentOptional.isPresent()) {
                if (contentOptional.get().equalsIgnoreCase("customize")) {
                    //TODO :
                    content = args.get(0);
                } else {
                    content = findClassMethodInvoke(contentOptional.get());
                }
            }
            newUrl.append(content);
            newUrl.append("&");
        }
        return newUrl.toString();
    }
}
