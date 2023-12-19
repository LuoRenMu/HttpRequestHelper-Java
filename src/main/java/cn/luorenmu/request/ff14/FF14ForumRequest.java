package cn.luorenmu.request.ff14;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.request.HttpRequest;
import cn.luorenmu.entiy.RequestType;
import cn.luorenmu.entiy.config.Request;
import cn.luorenmu.request.ff14.entiy.*;
import com.alibaba.fastjson2.JSON;

/**
 * @author LoMu
 * Date 2023.12.19 16:37
 */
public class FF14ForumRequest {
    private static final Request.RequestFF14 request = FileManager.getConfig(Request.class).getFf14();


    /**
     * 任务详情
     *
     * @return bool
     */
    public FF14MyTaskInfoResponse myTaskInfo() {
        HttpResponse response = ff14Request(request.getMyTaskInfo());
        return JSON.parseObject(response.body(), FF14MyTaskInfoResponse.class);

    }

    public FF14Response comment() {
        HttpResponse response = ff14Request(request.getComment());
        return JSON.parseObject(response.body(), FF14Response.class);
    }

    private HttpResponse ff14Request(Request.RequestDetailed requestDetailed, String... args) {
        requestDetailed.setRequestType(RequestType.FF14);
        return HttpRequest.execute(requestDetailed, args);
    }

    public boolean doSeal(String num) {
        HttpResponse response = ff14Request(request.getDoSeal(), num);
        FF14Response ff14Response = JSON.parseObject(response.body(), FF14Response.class);
        return ff14Response.getCode() == 10000;
    }

    public boolean getSealReward(String num) {
        HttpResponse response = ff14Request(request.getGetSealReward(), num);
        FF14Response ff14Response = JSON.parseObject(response.body(), FF14Response.class);

        return ff14Response.getCode() == 10000;
    }

    public boolean like() {
        HttpResponse response = ff14Request(request.getLike());
        FF14Response ff14Response = JSON.parseObject(response.body(), FF14LikeResponse.class);

        return ff14Response.getCode() == 10000;
    }

    public FF14MySignLogResponse mySignLog() {
        HttpResponse response = ff14Request(request.getMySignLog());
        return JSON.parseObject(response.body(), FF14MySignLogResponse.class);

    }

    public boolean signRewardList() {
        HttpResponse response = ff14Request(request.getSignRewardList());
        FF14SignRewardListResponse ff14SignRewardListResponse = JSON.parseObject(response.body(), FF14SignRewardListResponse.class);


        return ff14SignRewardListResponse.getCode() == 10000;
    }

    public FF14SignInResponse signIn() {
        HttpResponse response = ff14Request(request.getSignIn());
        return JSON.parseObject(response.body(), FF14SignInResponse.class);

    }


}
