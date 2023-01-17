package bg.sofia.uni.fmi.mjt.logger;

import java.io.IOException;

public class LogException extends IllegalStateException {
    public LogException(String msg) {
        super(msg);
    }
}
