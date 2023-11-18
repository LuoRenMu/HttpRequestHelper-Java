package cn.luorenmu.mihoyo;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.luorenmu.mihoyo.entiy.data.ForumCollectListInfo;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.11.13 4:09
 */
public class MihoyoForumService {


    public static void main(String[] args) {
        MihoyoForumService mihoyoForumService = new MihoyoForumService();
        System.out.println(mihoyoForumService.getCollectionPostList());

    }

    private ForumCollectListInfo getCollectionPostList() {
        HttpRequest httpRequest = HttpRequest.get("https://bbs-api.miyoushe.com/collection/api/collection/getCollectionPostList?collection_id=2058162&order_type=1");
        httpRequest.headerMap(Map.of("DS", getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1"), true);
        HttpResponse execute = httpRequest.execute();
        return JSON.parseObject(execute.body(), ForumCollectListInfo.class);
    }

    public boolean isRecentArticle() {
        return false;
    }

    private String getDS() {
        String i = (System.currentTimeMillis() / 1000) + "";
        String r = getRandomStr();
        return createDS("uTUzziiV9FazyGA7XgVIk287ZczinFRV", i, r);
    }

    /**
     * 获取随机字符串用于DS算法中
     *
     * @return String
     */
    private String getRandomStr() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 6; i++) {
            String CONSTANTS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            int number = random.nextInt(CONSTANTS.length());
            char charAt = CONSTANTS.charAt(number);
            sb.append(charAt);
        }
        return sb.toString();
    }

    /**
     * 创建DS算法
     *
     * @param n (<a href="https://github.com/UIGF-org/mihoyo-api-collect/issues/1">salt</a>)
     * @param i time
     * @param r random String
     * @return String
     */
    private String createDS(String n, String i, String r) {
        String c = DigestUtils.md5Hex("salt=" + n + "&t=" + i + "&r=" + r);
        return String.format("%s,%s,%s", i, r, c);
    }
}
