package cn.luorenmu.config;

import cn.luorenmu.entiy.Request;
import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

/**
 * @author LoMu
 * Date 2023.12.26 19:44
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class MihoyoRequest {
    private Request.RequestDetailed articleCollect;
    private Request.RequestDetailed article;
    private Request.RequestDetailed accessDevice;
    private Request.RequestPass requestPass;
}
