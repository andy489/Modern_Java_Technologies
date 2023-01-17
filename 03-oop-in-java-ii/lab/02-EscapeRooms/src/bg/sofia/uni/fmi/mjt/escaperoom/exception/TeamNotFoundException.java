package bg.sofia.uni.fmi.mjt.escaperoom.exception;

public class TeamNotFoundException extends Exception {
    public TeamNotFoundException(String msg) {
        super(msg);
    }

    public TeamNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
