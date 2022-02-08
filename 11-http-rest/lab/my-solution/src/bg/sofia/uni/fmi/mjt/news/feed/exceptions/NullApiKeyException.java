package bg.sofia.uni.fmi.mjt.news.feed.exceptions;

public class NullApiKeyException extends IllegalArgumentException {
    public NullApiKeyException(String msg) {
        super(msg);
    }
}
