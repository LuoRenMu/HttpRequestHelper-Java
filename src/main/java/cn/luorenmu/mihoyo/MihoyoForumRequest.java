package cn.luorenmu.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.convert.RequestContentConvert;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.utils.StringUtil;
import cn.luorenmu.entiy.Request;
import cn.luorenmu.mihoyo.entiy.data.ForumArticle;
import cn.luorenmu.mihoyo.entiy.data.ForumCollectList;
import cn.luorenmu.notification.ServerChanNotification;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LoMu
 * Date 2023.11.13 4:09
 */
public class MihoyoForumRequest {
    private static final Request request = FileManager.getConfig(Request.class);
    private static String cache;


    private ForumArticle getFourmArticle(String postId) {
        HttpRequest httpRequest = HttpRequest.get("https://bbs-api.miyoushe.com/post/api/getPostFull?post_id=" + postId + "&csm_source=search");
        httpRequest.headerMap(Map.of("DS", getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1", "Referer", " https://app.mihoyo.com"), true);
        HttpResponse execute = httpRequest.execute();
        System.out.println(execute.body());
        return JSON.parseObject(execute.body(), ForumArticle.class);
    }

    public void isRecentArticle() {
        ForumCollectList.ForumArticleSimple forumArticleSimple = getCollectionPostList().getData().getList().get(0);
        long createdAt = forumArticleSimple.getCreatedAt();
        long differTime = System.currentTimeMillis() / 1000 - createdAt;
        System.out.println("暂无新兑换码");
        if (differTime <= 60 * 60 * 10) {
            ForumArticle forumArticle = getFourmArticle(forumArticleSimple.getPostId());
            String redeemCodes = getRedeemCodes(forumArticle.getData().getPost().getPost().getContent());
            if (!Objects.equals(cache, redeemCodes)) {
                cache = redeemCodes;
                ServerChanNotification.sendMessageTitle("新版本兑换码已发放:" + redeemCodes);
            }
        }
    }

    private String getRedeemCodes(String content) {
        String pattern = "(<p>[A-Z0-9]{12}</p>)+";
        Pattern r = Pattern.compile(pattern);
        System.out.println(content);
        Matcher matcher = r.matcher(content);
        if (matcher.find()) {
            String group = matcher.group();
            String s = group.replaceAll("(^<p>)|(</p>$)", "");
            return s.replaceAll("</p><p>", ",");
        }
        return null;
    }

    protected static String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = StringUtil.getRandomStr(6);
        return createDS("uTUzziiV9FazyGA7XgVIk287ZczinFRV", i, r);
    }



    /**
     * 创建DS算法
     *
     * @param n (<a href="https://github.com/UIGF-org/mihoyo-api-collect/issues/1">salt</a>)
     * @param i time
     * @param r random String
     * @return String
     */
    private static String createDS(String n, String i, String r) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r);
        return String.format("%s,%s,%s", i, r, c);
    }

    private ForumCollectList getCollectionPostList() {
        HttpRequest httpRequest = HttpRequest.get(RequestContentConvert.requestToGet(request.getMihoyo().getForum().getArticleCollect()));
        httpRequest.headerMap(Map.of("DS", getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1"), true);
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), ForumCollectList.class);
    }
}
