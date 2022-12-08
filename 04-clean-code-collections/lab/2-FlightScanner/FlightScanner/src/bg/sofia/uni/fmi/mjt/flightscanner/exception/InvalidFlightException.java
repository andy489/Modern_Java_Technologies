package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class InvalidFlightException extends RuntimeException {
    public InvalidFlightException(String msg) {
        super(msg);
    }

    public InvalidFlightException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
