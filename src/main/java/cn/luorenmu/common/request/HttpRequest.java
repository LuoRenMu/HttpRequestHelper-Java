package cn.luorenmu.common.request;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.convert.RequestContentConvert;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.entiy.config.Request;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @author LoMu
 * Date 2023.12.02 20:32
 */

//进一步封装Http请求

@Slf4j
public class HttpRequest {

    /**
     * 同步锁用于避免多个线程同时对同一个api发起多次请求导致ip被封禁
     * 在每次请求发起前都会休眠5秒(睡眠不会让出线程锁)
     *
     * @param requestDetailed RequestDetailed
     * @return HttpResponse
     */
    public synchronized static HttpResponse execute(Request.RequestDetailed requestDetailed) {
        String requestDetailedMethodmethod = requestDetailed.getMethod();
        if (requestDetailedMethodmethod == null) {
            log.error("{} method is none  replace default GET method", requestDetailed);
            requestDetailedMethodmethod = "get";
        }
        RequestContentConvert requestContentConvert = new RequestContentConvert(requestDetailed);
        try {
            cn.hutool.http.HttpRequest httpRequest = (cn.hutool.http.HttpRequest) RequestContentConvert.class
                    .getMethod("requestTo" + StringUtils.firstCharacterUpperCaseOtherLowerCase(requestDetailedMethodmethod))
                    .invoke(requestContentConvert);

            log.debug("HTTPRequest: {} ", httpRequest);
            TimeUnit.SECONDS.sleep(5L);
            HttpResponse response = httpRequest.execute();
            log.debug("HTTPResponse: {}", response);
            return response;
        } catch (NoSuchMethodException e) {
            log.error("not support request method : {}", requestDetailed);
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
