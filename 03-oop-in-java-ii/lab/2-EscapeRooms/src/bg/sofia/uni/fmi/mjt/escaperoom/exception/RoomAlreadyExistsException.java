package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomAlreadyExistsException extends Exception{
    public RoomAlreadyExistsException(String msg){
        super(msg);
    }

    public RoomAlreadyExistsException(String msg, Throwable cause){
        super(msg, cause);
    }
}
