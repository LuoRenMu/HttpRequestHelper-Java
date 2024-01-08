package cn.luorenmu.entiy.config;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

/**
 * @author LoMu
 * Date 2023.12.26 19:44
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class Ff14Request {
    private Request.RequestDetailed myTaskInfo;
    private Request.RequestDetailed mySignLog;
    private Request.RequestDetailed signIn;
    private Request.RequestDetailed signRewardList;
    private Request.RequestDetailed comment;
    private Request.RequestDetailed like;
    private Request.RequestDetailed doSeal;
    private Request.RequestDetailed getSealReward;
    private Request.RequestDetailed isLogin;
    private Request.RequestDetailed itemList;
}
