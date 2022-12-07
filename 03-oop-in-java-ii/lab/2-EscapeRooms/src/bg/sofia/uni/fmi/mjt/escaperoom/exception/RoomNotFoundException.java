package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class RoomNotFoundException extends Exception{
    public RoomNotFoundException(String msg){
        super(msg);
    }

    public RoomNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}
