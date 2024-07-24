package cn.luorenmu.entiy;


import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.11.21 21:12
 */
@Data
public class Request {

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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestParam {
        private String name;
        private String content;

        public RequestParam(RequestParam param) {
            this.name = param.name;
            this.content = param.content;
        }

    }
}
