package cn.luorenmu.request.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.MatcherData;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.entiy.config.Request;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.notification.Notification;
import cn.luorenmu.notification.impl.ServerChanNotification;
import cn.luorenmu.request.mihoyo.entiy.data.ForumArticle;
import cn.luorenmu.request.mihoyo.entiy.data.ForumCollectList;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author LoMu
 * Date 2023.11.13 4:09
 */

@Slf4j
public class MihoyoForumRequest {
    private static final Request request = FileManager.getConfig(Request.class);
    private static final Map<String, String> cache = new HashMap<>();



    private ForumArticle getFourmArticle(String postId) {
        HttpRequest httpRequest = HttpRequest.get("https://bbs-api.miyoushe.com/post/api/getPostFull?post_id=" + postId + "&csm_source=search");
        httpRequest.headerMap(Map.of("DS", StringUtils.getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1", "Referer", " https://app.mihoyo.com"), true);
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), ForumArticle.class);
    }

    public void isRecentArticle() {
        List<ForumCollectList.ForumArticleSimple> list = getCollectionPostList().getData().getList();
        ForumCollectList.ForumArticleSimple forumArticleSimple = Collections.max(list, (t1, t2) -> (int) (t1.getCreatedAt() - t2.getCreatedAt()));
        Setting.Account account = RunningStorage.accountThreadLocal.get();
        Notification notification = new ServerChanNotification(account.getNotification().getServerChanKey());
        long createdAt = forumArticleSimple.getCreatedAt();
        long createdDifferTime = System.currentTimeMillis() / 1000 - createdAt;
        if (createdDifferTime <= 60 * 60 * 24 * 7) {
            ForumArticle forumArticle = getFourmArticle(forumArticleSimple.getPostId());
            long updatedAt = forumArticle.getData().getPost().getPost().getUpdatedAt();
            long updatedDifferTime = System.currentTimeMillis() / 1000 - updatedAt;
            if (updatedDifferTime <= 60 * 60 * 10) {
                Optional<String> redeemCodesOptional = MatcherData.redeemCodes(forumArticle.getData().getPost().getPost().getContent());
                String redeemCodes;
                if (redeemCodesOptional.isEmpty()) {
                    log.info("兑换码最新文章已出现,目前兑换码为null");
                    if (cache.get("empty") == null || System.currentTimeMillis() / 1000 - Long.parseLong(cache.get("empty")) > 60 * 60 * 24) {
                        notification.sendLongNotification("意外:兑换码最新文章已出现", "目前兑换码为null");
                        cache.put("empty", System.currentTimeMillis() / 1000 + "");
                    }
                    return;
                }
                redeemCodes = redeemCodesOptional.get();

                if (!Objects.equals(cache.get("redeemCodes"), redeemCodes)) {
                    cache.put("redeemCodes", redeemCodes);

                    log.info("兑换码 : {}", redeemCodes);
                    notification.sendLongNotification("新版本兑换码已发放", redeemCodes);
                }
            }
        }
    }


    private ForumCollectList getCollectionPostList() {
        String execute = cn.luorenmu.common.request.HttpRequest.execute(request.getMihoyo().getArticleCollect());
        return JSON.parseObject(execute, ForumCollectList.class);
    }
}
