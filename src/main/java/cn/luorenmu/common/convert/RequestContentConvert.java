package cn.luorenmu.common.convert;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.MatchData;
import cn.luorenmu.entiy.config.Request;
import com.alibaba.fastjson2.JSON;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author LoMu
 * Date 2023.11.25 18:56
 */

@Accessors(chain = true)
public class RequestContentConvert {
    private final HttpRequest HTTP_REQUEST = HttpRequest.of("12312312");
    @Setter
    private Request.RequestDetailed requestDetailed;

    public RequestContentConvert(Request.RequestDetailed requestDetailed) {
        this.requestDetailed = requestDetailed;
        replaceAddData();
    }

    public static void main(String[] args) {
        HttpRequest httpRequest = new RequestContentConvert(FileManager.getConfig(Request.class).getMihoyo().getArticleCollect()).requestToGet();
        HttpResponse execute = httpRequest.execute();
        System.out.println(httpRequest);
        System.out.println(execute.body());
        System.out.println(FileManager.getConfig(Request.class).getMihoyo().getArticleCollect());
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
        List<Request.RequestParam> newParams = new ArrayList<>();
        for (Request.RequestParam param : params) {
            Request.RequestParam newParam = new Request.RequestParam(param);
            Optional<String> s = MatchData.scanReplaceFieldName(param.getContent());

            if (s.isEmpty()) {
                newParams.add(newParam);
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
                newParam.setContent(invoke);


            } catch (ClassNotFoundException ignored) {

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } finally {
                newParams.add(newParam);
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

    private String paramToUrl(String url, List<Request.RequestParam> requestParams) {
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
