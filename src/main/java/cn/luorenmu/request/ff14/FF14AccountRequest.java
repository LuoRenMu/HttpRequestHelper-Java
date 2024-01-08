package cn.luorenmu.request.ff14;

import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Ff14Request;
import cn.luorenmu.request.ff14.entiy.FF14IsLoginResponse;
import com.alibaba.fastjson2.JSON;

/**
 * @author LoMu
 * Date 2023.12.20 22:23
 */
public class FF14AccountRequest {
    private static final Ff14Request request = FileManager.getConfig(Ff14Request.class);

    /**
     * 获取当前Cookie用户信息
     *
     * @return FF14IsLoginResponse
     */
    public FF14IsLoginResponse isLogin() {
        return JSON.parseObject(FF14ForumRequest.ff14Request(request.getIsLogin()), FF14IsLoginResponse.class);
    }


}
