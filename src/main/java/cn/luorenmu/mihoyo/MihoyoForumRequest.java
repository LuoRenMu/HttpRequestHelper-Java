package cn.luorenmu.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.MatchData;
import cn.luorenmu.common.utils.StringUtils;
import cn.luorenmu.entiy.config.Request;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.mihoyo.entiy.data.ForumArticle;
import cn.luorenmu.mihoyo.entiy.data.ForumCollectList;
import cn.luorenmu.notification.ServerChanNotification;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @author LoMu
 * Date 2023.11.13 4:09
 */

@Slf4j
public class MihoyoForumRequest {
    private static final Request request = FileManager.getConfig(Request.class);
    private static String cache;


    private ForumArticle getFourmArticle(String postId) {
        HttpRequest httpRequest = HttpRequest.get("https://bbs-api.miyoushe.com/post/api/getPostFull?post_id=" + postId + "&csm_source=search");
        httpRequest.headerMap(Map.of("DS", StringUtils.getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1", "Referer", " https://app.mihoyo.com"), true);
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), ForumArticle.class);
    }

    public void isRecentArticle() {
        ForumCollectList.ForumArticleSimple forumArticleSimple = getCollectionPostList().getData().getList().get(0);
        Setting.Account account = RunningStorage.accountThreadLocal.get();
        log.info("线程 {} 已托管账户: {}", Thread.currentThread().getName(), account.toString());
        long createdAt = forumArticleSimple.getCreatedAt();
        long differTime = System.currentTimeMillis() / 1000 - createdAt;
        if (differTime <= 60 * 60 * 10) {
            ForumArticle forumArticle = getFourmArticle(forumArticleSimple.getPostId());
            String redeemCodes = MatchData.redeemCodes(forumArticle.getData().getPost().getPost().getContent()).orElse("内部错误 > 兑换码获取失败");
            if (!Objects.equals(cache, redeemCodes)) {
                cache = redeemCodes;
                log.info("兑换码 : {}", redeemCodes);
                ServerChanNotification.sendTitleAndMessage("新版本兑换码已发放", redeemCodes);
            }
        }
    }





    private ForumCollectList getCollectionPostList() {
        HttpResponse execute = cn.luorenmu.common.utils.HttpRequest.execute(request.getMihoyo().getArticleCollect());
        return JSON.parseObject(execute.body(), ForumCollectList.class);
    }
}
