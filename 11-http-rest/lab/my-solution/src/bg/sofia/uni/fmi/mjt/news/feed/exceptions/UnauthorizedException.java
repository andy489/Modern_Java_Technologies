package bg.sofia.uni.fmi.mjt.news.feed.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
