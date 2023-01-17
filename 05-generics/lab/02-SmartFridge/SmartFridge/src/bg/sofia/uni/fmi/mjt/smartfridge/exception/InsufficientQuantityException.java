package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String msg) {
        super(msg);
    }

    public InsufficientQuantityException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
