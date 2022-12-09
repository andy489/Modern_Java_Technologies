package bg.sofia.uni.fmi.mjt.smartfridge.exception;

public class FridgeCapacityExceededException extends Exception {
    public FridgeCapacityExceededException(String msg) {
        super(msg);
    }

    public FridgeCapacityExceededException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
