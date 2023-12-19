package cn.luorenmu.request.ff14.entiy;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author LoMu
 * Date 2023.12.19 18:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14SignRewardListResponse extends FF14Response {
    private List<SignRewardListData> data;

    /**
     * "id": 2,
     * "begin_date": "2023-11-01 00:00:00",
     * "end_date": "2024-12-31 23:59:59",
     * "rule": 20,
     * "item_name": "白银陆行鸟的羽毛X5",
     * "item_pic": "https://fu5.web.sdo.com/10036/202312/17025420917106.png",
     * "num": 1,
     * "item_desc": "本月签到20天奖励",
     * "is_get": -1
     */
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class SignRewardListData {
        private int id;
        private String beginDate;
        private String endDate;
        private int rule;
        private String itemName;
        private String itemPic;
        private int num;
        private String itemDesc;
        private int isGet;
    }
}
