package cn.luorenmu.common.request;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
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
    private final static String NONE_RESPONSE_STR = """
            {
              "code": %s,
              "msg": "%s"
            }
            """;

    /**
     * 同步锁用于避免多个线程同时对同一个api发起多次请求导致ip被封禁
     * 在每次请求发起前都会休眠5秒(睡眠不会让出线程锁)
     *
     * @param requestDetailed RequestDetailed
     * @return HttpResponse
     */
    public synchronized static String execute(Request.RequestDetailed requestDetailed, String... args) {
        String requestDetailedMethodmethod = requestDetailed.getMethod();
        if (requestDetailedMethodmethod == null) {
            log.error("{} method is none  replace default GET method", requestDetailed);
            requestDetailedMethodmethod = "get";
        }
        RequestContentConvert requestContentConvert = new RequestContentConvert(requestDetailed, args);
        try {
            cn.hutool.http.HttpRequest httpRequest = (cn.hutool.http.HttpRequest) RequestContentConvert.class
                    .getMethod("requestTo" + StringUtils.firstCharacterUpperCaseOtherLowerCase(requestDetailedMethodmethod))
                    .invoke(requestContentConvert);

            TimeUnit.SECONDS.sleep(5L);
            HttpResponse response = httpRequest.execute();
            String body = response.body();
            log.debug("HTTPRequest: {} ", httpRequest);
            log.debug("HTTPResponse: {}", response);

            if (response.getStatus() != HttpStatus.HTTP_OK) {
                log.error("请求状态与预期状态不符 : {} \n {}", requestDetailed, response);
                return String.format(NONE_RESPONSE_STR, response.getStatus(), body.replaceAll("\"", ""));
            }
            return body;
        } catch (NoSuchMethodException e) {
            log.error("not support request method : {}", requestDetailed);
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
