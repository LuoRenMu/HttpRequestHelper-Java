package cn.luorenmu.mihoyo.entiy.account;

import cn.luorenmu.mihoyo.entiy.Games;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author LoMu
 * Date 2023.10.28 16:08
 */

@Accessors(chain = true)
@Data
public class SignInUser {
    private Games games;
    private String email;
    @Setter(AccessLevel.NONE)
    private String uidParam;
    @Setter(AccessLevel.NONE)
    private String uid;
    private String cookie;


    public SignInUser setUid(String uid) {
        this.uid = uid;
        this.uidParam = "&uid=" + uid;
        return this;
    }
}
