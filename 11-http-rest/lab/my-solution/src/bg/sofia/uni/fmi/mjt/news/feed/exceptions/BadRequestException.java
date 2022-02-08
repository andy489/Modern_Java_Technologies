package bg.sofia.uni.fmi.mjt.news.feed.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
