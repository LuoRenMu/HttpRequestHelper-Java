package cn.luorenmu.request.ff14;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Request;
import cn.luorenmu.request.ff14.entiy.FF14IsLoginResponse;
import com.alibaba.fastjson2.JSON;

/**
 * @author LoMu
 * Date 2023.12.20 22:23
 */
public class FF14AccountRequest {
    private static final Request.RequestFF14 request = FileManager.getConfig(Request.class).getFf14();

    /**
     * 获取当前Cookie用户信息
     *
     * @return FF14IsLoginResponse
     */
    public FF14IsLoginResponse isLogin() {
        HttpResponse response = FF14ForumRequest.ff14Request(request.getIsLogin());
        return JSON.parseObject(response.body(), FF14IsLoginResponse.class);
    }


}
