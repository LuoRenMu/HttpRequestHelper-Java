package cn.luorenmu.request.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.request.HttpRequestUtils;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.config.MihoyoRequest;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.request.mihoyo.entiy.data.ForumArticle;
import cn.luorenmu.request.mihoyo.entiy.data.ForumCollectList;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.13 4:09
 */

@Slf4j
public class MihoyoForumRequest {


    private static final MihoyoRequest request = RunStorage.getConfig(MihoyoRequest.class);


    public ForumArticle getFourmArticle(String postId) {
        HttpRequest httpRequest = HttpRequest.get("https://bbs-api.miyoushe.com/post/api/getPostFull?post_id=" + postId + "&csm_source=search");
        httpRequest.headerMap(Map.of("DS", StringUtils.getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1", "Referer", " https://app.mihoyo.com"), true);
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), ForumArticle.class);
    }


    public ForumCollectList getCollectionPostList() {
        String execute = HttpRequestUtils.execute(request.getArticleCollect());
        return JSON.parseObject(execute, ForumCollectList.class);
    }
}
