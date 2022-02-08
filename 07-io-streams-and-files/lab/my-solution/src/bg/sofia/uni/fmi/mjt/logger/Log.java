package bg.sofia.uni.fmi.mjt.logger;

import java.time.LocalDateTime;

public record Log(Level level, LocalDateTime timestamp, String packageName, String message) {
    @Override
    public String toString() {
        return String.format("[%s]|%s|%s|%s%n", level, timestamp, packageName, message);
    }
}