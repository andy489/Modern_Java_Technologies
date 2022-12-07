package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class PlatformCapacityExceededException extends RuntimeException{
    public PlatformCapacityExceededException(String msg){
        super(msg);
    }

    public PlatformCapacityExceededException(String msg, Throwable cause){
        super(msg, cause);
    }
}
