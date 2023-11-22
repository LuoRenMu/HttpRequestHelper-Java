package cn.luorenmu.entiy;

import lombok.Data;

/**
 * @author LoMu
 * Date 2023.11.21 21:12
 */
@Data
public class UnifiedRequest {
    private String url;
    private String method;

    @Data
    public static class RequestParam {
        private String name;
        private String content;
    }
}
