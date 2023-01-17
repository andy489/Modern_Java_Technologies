package bg.sofia.uni.fmi.mjt.logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultLogParser implements LogParser {
    private static final String LINE_SPLIT_REG_EX = "\\|";
    private static final String REM_STR_BRA_REG_EX = "[\\]\\[]";
    private static final String EMPTY_REPLACER = "";

    private static final int LVL_IND = 0;
    private static final int T_STAMP_IND = 1;
    private static final int PAC_NAME_IND = 2;
    private static final int MSG_IND = 3;

    protected static final int ATT_CNT = 4;


    private final Path logsFilePath;

    public DefaultLogParser(Path logsFilePath) {
        this.logsFilePath = logsFilePath;
    }

    @Override
    public List<Log> getLogs(Level level) {
        if (level == null) {
            throw new IllegalArgumentException("Failed to getLogs");
        }

        List<Log> extractedLogs = new ArrayList<>();

        try (var br = new BufferedReader(new FileReader(String.valueOf(logsFilePath)))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] attributes = line.split(LINE_SPLIT_REG_EX);

                String lvl = attributes[LVL_IND].replaceAll(REM_STR_BRA_REG_EX, EMPTY_REPLACER);

                LocalDateTime timestamp = LocalDateTime.parse(attributes[T_STAMP_IND]);
                String packageName = attributes[PAC_NAME_IND];
                String msg = attributes[MSG_IND];

                if (lvl.equals(level.name())) {
                    extractedLogs.add(new Log(level, timestamp, packageName, msg));
                }
            }
        } catch (IOException cause) {
            throw new LogException("Could not find/read from file.");
        }

        return extractedLogs;
    }

    @Override
    public List<Log> getLogs(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid args for getLogs");
        }

        List<Log> extractedLogs = new ArrayList<>();

        try (var br = new BufferedReader(new FileReader(String.valueOf(logsFilePath)))) {
            for (String line; (line = br.readLine()) != null; ) {
                String[] attributes = line.split(LINE_SPLIT_REG_EX);

                String lvl = attributes[LVL_IND].replaceAll(REM_STR_BRA_REG_EX, EMPTY_REPLACER);

                LocalDateTime timestamp = LocalDateTime.parse(attributes[T_STAMP_IND]);

                String packageName = attributes[PAC_NAME_IND];
                String msg = attributes[MSG_IND];

                boolean inside = timestamp.isAfter(from) && timestamp.isBefore(to);
                boolean limits = timestamp.equals(from) || timestamp.equals(to);

                if (inside || limits) {
                    extractedLogs.add(new Log(Level.valueOf(lvl), timestamp, packageName, msg));
                }
            }
        } catch (IOException cause) {
            throw new LogException("Could not find/read from file.");
        }

        return extractedLogs;
    }

    @Override
    public List<Log> getLogsTail(int n) {
        if (n == 0) {
            return List.of();
        }

        if (n < 0) {
            throw new IllegalArgumentException("Invalid arg for getLogsTail");
        }

        List<Log> tail = new ArrayList<>();

        long linesCount = 0;

        try (var br = new BufferedReader(new FileReader(String.valueOf(logsFilePath)))) {
            while (br.readLine() != null) {
                linesCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long skipped = linesCount - n;

        try (var br = new BufferedReader(new FileReader(String.valueOf(logsFilePath)))) {
            int counter = 0;

            for (String line; (line = br.readLine()) != null; ) {
                String[] attributes = line.split(LINE_SPLIT_REG_EX);

                if (attributes.length != ATT_CNT) {
                    throw new LogException("Invalid log");
                }

                String lvl = attributes[LVL_IND].replaceAll(REM_STR_BRA_REG_EX, EMPTY_REPLACER);
                LocalDateTime timestamp = LocalDateTime.parse(attributes[T_STAMP_IND]);
                String packageName = attributes[PAC_NAME_IND];
                String msg = attributes[MSG_IND];

                if (counter >= skipped) {
                    tail.add(new Log(Level.valueOf(lvl), timestamp, packageName, msg));
                }

                counter++;
            }
        } catch (IOException cause) {
            throw new LogException("Could not find/read from file.");
        }

        return tail;
    }
}
