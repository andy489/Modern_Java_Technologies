package bg.sofia.uni.fmi.mjt.news.feed.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String msg) {
        super(msg);
    }
}
