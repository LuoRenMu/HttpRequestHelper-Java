package cn.luorenmu.mihoyo.entiy.data;

import cn.luorenmu.mihoyo.entiy.MihoyoResponse;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.11.18 19:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ForumArticle extends MihoyoResponse {
    private ArticleData data;

    @Data
    public static class ArticleData {
        private ArticlePost post;

        @Data
        public static class ArticlePost {
            private ArticleSubPost post;

            @Data
            @JSONType(naming = PropertyNamingStrategy.SnakeCase)
            public static class ArticleSubPost {
                private String content;
                private String gameId;
                private long createdAt;
                //标题
                private String subject;
                private String structuredontent;

            }
        }
    }
}
