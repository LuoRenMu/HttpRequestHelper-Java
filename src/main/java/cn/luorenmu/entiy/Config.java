package cn.luorenmu.entiy;

import lombok.Data;

/**
 * @author LoMu
 * Date 2023.10.28 19:36
 */


@Data
public class Config {
    private String cookie;
    private String serverChanKey;
    private String serverChanUrl = "https://sctapi.ftqq.com/";
    private String email;
}
