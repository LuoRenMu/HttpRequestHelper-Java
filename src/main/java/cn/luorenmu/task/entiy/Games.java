package cn.luorenmu.task.entiy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author LoMu
 * Date 2023.10.28 15:59
 */


public enum Games {
    STAR_RAIL(new GameInfo().setActId("e202304121516551").setGameBiz("hkrpg_cn").setRegion("prod_gf_cn"));


    private final GameInfo display;

    Games(GameInfo a) {
        display = a;
    }

    public String getActId() {
        return display.getActId();
    }

    public String getParam() {
        return display.getParam();
    }

    @Data
    @Accessors(chain = true)
    public static class GameInfo {
        private String actId;
        private String region;
        @Setter(AccessLevel.NONE)
        private String param;
        private String gameBiz;

        public GameInfo setActId(String actId) {
            this.actId = actId;
            this.param = String.format("&act_id=%s&region=%s", actId, region);
            return this;
        }
    }
}
