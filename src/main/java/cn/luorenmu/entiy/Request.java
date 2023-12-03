package cn.luorenmu.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LoMu
 * Date 2023.11.21 21:12
 */
@Data
public class Request {
    private RequestClassify mihoyo;

    @Data
    public static class RequestClassify {
        private RequestData forum;
    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class RequestData {
        private RequestDetailed articleCollect;
        private RequestDetailed article;
    }

    @Data
    public static class RequestDetailed {
        private String url;
        private String method;
        private List<RequestParam> params = new ArrayList<>();
        private List<RequestParam> body = new ArrayList<>();
        private List<RequestParam> headers = new ArrayList<>();
    }

    @Data
    public static class RequestParam {
        private String name;
        private String content;
    }
}
