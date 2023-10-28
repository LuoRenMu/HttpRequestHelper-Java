package cn.luorenmu.task.entiy;

/**
 * @author LoMu
 * Date 2023.10.28 15:59
 */


public enum Games {
    STAR_RAIL("&act_id=e202304121516551&region=prod_gf_cn");


    private final String display;

    Games(String string) {
        display = string;
    }

    public String getDisplay() {
        return display;
    }
}
