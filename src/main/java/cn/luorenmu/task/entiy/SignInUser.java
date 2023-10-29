package cn.luorenmu.task.entiy;

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
    private Long uid;
    private String cookie;


    public SignInUser setUid(Long uid) {
        this.uid = uid;
        this.uidParam = "&uid=" + uid;
        return this;
    }
}
