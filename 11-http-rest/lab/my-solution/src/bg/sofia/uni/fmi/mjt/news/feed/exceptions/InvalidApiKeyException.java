package bg.sofia.uni.fmi.mjt.news.feed.exceptions;

public class InvalidApiKeyException extends IllegalArgumentException {
    public InvalidApiKeyException(String msg) {
        super(msg);
    }
}
