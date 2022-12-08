package bg.sofia.uni.fmi.mjt.flightscanner.exception;

public class FlightCapacityExceededException extends Exception {
    public FlightCapacityExceededException(String msg) {
        super(msg);
    }

    public FlightCapacityExceededException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
