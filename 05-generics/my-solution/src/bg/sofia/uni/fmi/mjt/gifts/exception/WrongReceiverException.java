package bg.sofia.uni.fmi.mjt.gifts.exception;

public class WrongReceiverException extends IllegalArgumentException {
    public WrongReceiverException(String msg) {
        super(msg);
    }
}
