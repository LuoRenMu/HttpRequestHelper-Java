package cn.luorenmu.entiy;

import lombok.Data;

import java.util.Map;

/**
 * @author LoMu
 * Date 2024.05.09 10:01
 */
@Data
public class Requests {
    private Map<String, Request.RequestDetailed> params;
}
