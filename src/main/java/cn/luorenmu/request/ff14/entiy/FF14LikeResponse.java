package cn.luorenmu.request.ff14.entiy;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author LoMu
 * Date 2023.12.19 20:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FF14LikeResponse extends FF14Response {
    private int data;
}
