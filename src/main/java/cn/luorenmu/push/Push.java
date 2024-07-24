package cn.luorenmu.push;

/**
 * @author LoMu
 * Date 2023.12.17 13:38
 */
public interface Push {
    boolean executeShortMessage(String title);

    boolean executeLongMessage(String title, String message);
}
