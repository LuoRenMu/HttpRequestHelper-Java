package cn.luorenmu.common.request;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.entiy.Request;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @author LoMu
 * Date 2023.12.02 20:32
 */

//进一步封装Http请求

@Slf4j
public class HttpRequestCentre {

    /**
     * 同步锁用于避免多个线程同时对同一个api发起多次请求导致ip被封禁
     * 在每次请求发起前都会休眠5秒(睡眠不会让出线程锁)
     *
     * @param requestDetailed RequestDetailed
     * @return HttpResponse
     */
    public synchronized static HttpResponse execute(Request.RequestDetailed requestDetailed, String... args) {

        String requestDetailedMethodd = requestDetailed.getMethod();
        if (requestDetailedMethodd == null) {
            log.error("{} method is none  replace default GET method", requestDetailed);
            requestDetailedMethodd = "get";
        }
        RequestDetailedToHttpRequest requestContentConvert = new RequestDetailedToHttpRequest(requestDetailed, args);
        try {
            HttpRequest httpRequest = (HttpRequest) RequestDetailedToHttpRequest.class
                    .getMethod("requestTo" + StringUtils.firstCharacterUpperCaseOtherLowerCase(requestDetailedMethodd))
                    .invoke(requestContentConvert);

            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException e) {
                log.warn("sleep interrupted : {}", e.getMessage());
            }
            log.info("HTTPRequest: URL -> {}", httpRequest.getUrl());
            HttpResponse response = httpRequest.execute();

            if (response.getStatus() != HttpStatus.HTTP_OK) {
                log.error("请求状态与预期状态不符,结果无法正常响应: {} \n {} \n {}", requestDetailed, response, httpRequest);
                return null;
            }
            return response;
        } catch (NoSuchMethodException e) {
            log.error("not support request method : {}", requestDetailed);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException : {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (IORuntimeException e) {
            log.warn("与远程服务器连接发送错误 url -> {} <- 该请求无法正常被处理", requestDetailed.getUrl());
            return null;
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException : {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
