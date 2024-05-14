package cn.luorenmu.exception;

/**
 * @author LoMu
 * Date 2024.04.30 20:57
 */
public class MisconfigurationException extends RuntimeException {
    public MisconfigurationException(String message) {
        super(message);
    }
}
