package cn.luorenmu.request.ff14;

import cn.luorenmu.common.request.HttpRequestUtils;
import cn.luorenmu.config.Ff14Request;
import cn.luorenmu.entiy.Request;
import cn.luorenmu.entiy.RequestType;
import cn.luorenmu.entiy.RunStorage;
import cn.luorenmu.request.ff14.entiy.*;
import com.alibaba.fastjson2.JSON;

/**
 * @author LoMu
 * Date 2023.12.19 16:37
 */
public class FF14ForumRequest {
    private static final Ff14Request request = RunStorage.getConfig(Ff14Request.class);


    protected static String ff14Request(Request.RequestDetailed requestDetailed, String... args) {
        requestDetailed.setRequestType(RequestType.FF14);
        return HttpRequestUtils.execute(requestDetailed, args);
    }

    public FF14Response createDynamic() {
        return JSON.parseObject(ff14Request(request.getCreateDynamic()), FF14Response.class);
    }

    /**
     * 任务详情
     *
     * @return FF14MyTaskInfoResponse
     */
    public FF14MyTaskInfoResponse myTaskInfo() {
        return JSON.parseObject(ff14Request(request.getMyTaskInfo()), FF14MyTaskInfoResponse.class);

    }

    /**
     * 评论
     *
     * @return FF14Response
     */
    public FF14Response comment() {
        return JSON.parseObject(ff14Request(request.getComment()), FF14Response.class);
    }

    /**
     * 印章(任务完成)
     *
     * @param num 1 = 签到  2 = 点赞*5   3 = 评论
     * @return FF14Response
     */
    public FF14Response doSeal(String num) {
        return JSON.parseObject(ff14Request(request.getDoSeal(), num), FF14Response.class);
    }

    /**
     * 获取印章奖励
     *
     * @param num 印章天数(signRewardList)
     * @return FF14Response
     */
    public FF14Response getSealReward(String num) {
        return JSON.parseObject(ff14Request(request.getGetSealReward(), num), FF14Response.class);

    }

    /**
     * 点赞/取消点赞
     *
     * @return FF14Response
     */
    public FF14Response like() {
        return JSON.parseObject(ff14Request(request.getLike()), FF14LikeResponse.class);

    }

    /**
     * 印章奖励查询
     *
     * @return FF14ItemListResponse
     */
    public FF14ItemListResponse itemList() {
        return JSON.parseObject(ff14Request(request.getItemList()), FF14ItemListResponse.class);
    }


    /**
     * 签到日志
     *
     * @return FF14MySignLogResponse
     */
    public FF14MySignLogResponse mySignLog() {
        return JSON.parseObject(ff14Request(request.getMySignLog()), FF14MySignLogResponse.class);

    }

    /**
     * 查看签到奖励
     *
     * @return FF14SignRewardListResponse
     */
    public FF14SignRewardListResponse signRewardList() {
        return JSON.parseObject(ff14Request(request.getSignRewardList()), FF14SignRewardListResponse.class);


    }

    /**
     * 签到
     *
     * @return FF14SignInResponse
     */
    public FF14SignInResponse signIn() {
        return JSON.parseObject(ff14Request(request.getSignIn()), FF14SignInResponse.class);

    }


}
