package cn.luorenmu.task;

import cn.luorenmu.common.cache.FileCache;
import cn.luorenmu.common.utils.MatcherData;
import cn.luorenmu.common.utils.Notifications;
import cn.luorenmu.entiy.TaskCache;
import cn.luorenmu.request.mihoyo.MihoyoForumRequest;
import cn.luorenmu.request.mihoyo.entiy.data.ForumArticle;
import cn.luorenmu.request.mihoyo.entiy.data.ForumCollectList;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author LoMu
 * Date 2024.01.28 20:55
 */
@Slf4j
public class MihoyoTask {

    private static final MihoyoForumRequest request = new MihoyoForumRequest();

    public void isRecentArticle() {
        List<ForumCollectList.ForumArticleSimple> list = request.getCollectionPostList().getData().getList();
        ForumCollectList.ForumArticleSimple forumArticleSimple = Collections.max(list, (t1, t2) -> (int) (t1.getCreatedAt() - t2.getCreatedAt()));
        long createdAt = forumArticleSimple.getCreatedAt();
        long createdDifferTime = System.currentTimeMillis() / 1000 - createdAt;
        if (createdDifferTime <= 60 * 60 * 24 * 7) {
            ForumArticle forumArticle = request.getFourmArticle(forumArticleSimple.getPostId());
            long updatedAt = forumArticle.getData().getPost().getPost().getUpdatedAt();
            long updatedDifferTime = System.currentTimeMillis() / 1000 - updatedAt;
            if (updatedDifferTime <= 60 * 60 * 10) {
                Optional<String> redeemCodesOptional = MatcherData.redeemCodes(forumArticle.getData().getPost().getPost().getContent());
                String redeemCodes;
                if (redeemCodesOptional.isEmpty()) {
                    log.info("兑换码最新文章已出现,目前兑换码为null");
                    return;
                }
                redeemCodes = redeemCodesOptional.get();

                if (redeemCodes.split(",").length <= 2) {
                    return;
                }
                TaskCache taskCache = FileCache.readCache(Thread.currentThread().getName());
                if (!Objects.equals(taskCache.getStarRail(), redeemCodes)) {
                    taskCache.setStarRail(redeemCodes);
                    FileCache.writeCache(Thread.currentThread().getName(), taskCache);

                    log.info("兑换码 : {}", redeemCodes);
                    Notifications.sendAllNotification("星穹铁道新版本兑换码已发放", redeemCodes);
                }
            }
        }
    }
}
