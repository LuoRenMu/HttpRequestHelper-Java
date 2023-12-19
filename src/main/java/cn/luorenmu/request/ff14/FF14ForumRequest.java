package cn.luorenmu.request.ff14;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.annotation.impl.RunningStorage;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.common.request.HttpRequest;
import cn.luorenmu.entiy.RequestType;
import cn.luorenmu.entiy.config.Request;
import cn.luorenmu.entiy.config.Setting;
import cn.luorenmu.request.ff14.entiy.FF14MySignLogResponse;
import cn.luorenmu.request.ff14.entiy.FF14MyTaskInfoResponse;
import cn.luorenmu.request.ff14.entiy.FF14SignInResponse;
import com.alibaba.fastjson2.JSON;

/**
 * @author LoMu
 * Date 2023.12.19 16:37
 */
public class FF14ForumRequest {
    private static final Request.RequestFF14 request = FileManager.getConfig(Request.class).getFf14();

    public static void main(String[] args) {
        Setting.Account account = FileManager.getConfig(Setting.class).getAccounts().get(0);
        RunningStorage.accountThreadLocal.set(account);
        FF14ForumRequest ff14ForumRequest = new FF14ForumRequest();
        ff14ForumRequest.myTaskInfo();
    }

    /**
     * 任务详情
     *
     * @return bool
     */
    public boolean myTaskInfo() {
        HttpResponse response = ff14Request(request.getMyTaskInfo());
        FF14MyTaskInfoResponse ff14MyTaskInfo = JSON.parseObject(response.body(), FF14MyTaskInfoResponse.class);


        return ff14MyTaskInfo.getCode() == 10000;
    }

    private HttpResponse ff14Request(Request.RequestDetailed requestDetailed) {
        requestDetailed.setRequestType(RequestType.FF14);
        return HttpRequest.execute(requestDetailed);
    }

    public boolean mySignLog() {
        HttpResponse response = ff14Request(request.getMySignLog());
        FF14MySignLogResponse ff14MySignLogResponse = JSON.parseObject(response.body(), FF14MySignLogResponse.class);


        return ff14MySignLogResponse.getCode() == 10000;
    }

    public boolean Signin() {
        HttpResponse response = ff14Request(request.getSignIn());
        FF14SignInResponse ff14SignInResponse = JSON.parseObject(response.body(), FF14SignInResponse.class);


        return ff14SignInResponse.getCode() == 10000;
    }


}
