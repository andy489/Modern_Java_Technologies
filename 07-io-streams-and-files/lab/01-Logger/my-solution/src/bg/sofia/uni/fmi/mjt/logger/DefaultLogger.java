package bg.sofia.uni.fmi.mjt.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class DefaultLogger implements Logger {
    private static final String LOG_PREFIX = "logs-";
    private static final String LOG_SUFFIX = ".txt";
    private static final String LOG_FILES_REG_EX = "logs-[0-9]+\\.txt";

    private final LoggerOptions options;
    private Path currLogPath;
    private int id;

    public DefaultLogger(LoggerOptions options) {
        this.options = options;
        createLogFile();
    }

    private void createLogFile() {
        String path = options.getDirectory() + File.separator + LOG_PREFIX + id + LOG_SUFFIX;

        File f = new File(path);

        f.getParentFile().mkdirs();

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        currLogPath = Paths.get(path);

        id++;
    }

    private void writeToFile(Path filePath, String text, boolean throwsErrors) {
        try (var bw = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            bw.write(text);
            bw.flush();
        } catch (IOException e) {
            if (throwsErrors) throw new LogException("Could not write to file");
        }
    }

    @Override
    public void log(Level level, LocalDateTime timestamp, String message) {
        if (level == null) {
            throw new IllegalArgumentException("~Invalid arg for log");
        }

        if (timestamp == null) {
            throw new IllegalArgumentException("~Invalid arg for log");
        }

        if (message == null) {
            throw new IllegalArgumentException("~Invalid arg for log");
        }

        if (message.isEmpty()) {
            throw new IllegalArgumentException("~Invalid arg for log");
        }

        boolean ignore = options.getMinLogLevel().getLevel() > level.getLevel();

        if (ignore) {
            return;
        }

        String logMsg = new Log(level, timestamp, options.getClazz().getPackageName(), message).toString();

        long currLogSize = 0;

        try {
            currLogSize = Files.size(currLogPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long sizeOfFileAfterLog = logMsg.length() + currLogSize;

        if (sizeOfFileAfterLog > options.getMaxFileSizeBytes()) {
            createLogFile();
        }

        writeToFile(currLogPath, logMsg, options.shouldThrowErrors());
    }

    @Override
    public LoggerOptions getOptions() {
        return options;
    }

    @Override
    public Path getCurrentFilePath() {
        return currLogPath;
    }
}
