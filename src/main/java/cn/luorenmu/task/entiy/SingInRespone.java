package cn.luorenmu.task.entiy;

import lombok.Data;


/**
 * @author LoMu
 * Date 2023.10.28 20:17
 */
@Data
public class SingInRespone {
    private int retcode;
    private String message;
    private Data data;

    @lombok.Data
    public static class Data {
        private int total_sign_day;
        private String today;
    }
}
