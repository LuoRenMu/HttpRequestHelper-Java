package cn.luorenmu.entiy.config;

import cn.luorenmu.annotation.Value;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.11.21 21:12
 */
@Data
public class Request {
    @Value(name = "")
    private RequestClassify mihoyo;


    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class RequestClassify {
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

    }

    @Data
    public static class RequestParam {
        private String name;
        private String content;
    }
}
