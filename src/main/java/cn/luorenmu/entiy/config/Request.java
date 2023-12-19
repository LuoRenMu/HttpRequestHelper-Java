package cn.luorenmu.entiy.config;


import cn.luorenmu.entiy.RequestType;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.11.21 21:12
 */
@Data
public class Request {
    private RequestMihoyo mihoyo;
    private RequestFF14 ff14;


    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class RequestFF14 {
        private RequestDetailed myTaskInfo;
        private RequestDetailed mySignLog;
        private RequestDetailed signIn;


    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class RequestMihoyo {
        private RequestDetailed articleCollect;
        private RequestDetailed article;
        private RequestDetailed accessDevice;
        private RequestPass requestPass;
    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class RequestPass {
        private String rsPublicKey;
    }

    @Data
    public static class RequestDetailed {
        private String url;
        private String method;
        private List<RequestParam> params;
        private List<RequestParam> body;
        private List<RequestParam> headers;
        private RequestType requestType;
    }

    @Data
    @NoArgsConstructor
    public static class RequestParam {
        private String name;
        private String content;

        public RequestParam(RequestParam param) {
            this.name = param.name;
            this.content = param.content;
        }
    }
}
