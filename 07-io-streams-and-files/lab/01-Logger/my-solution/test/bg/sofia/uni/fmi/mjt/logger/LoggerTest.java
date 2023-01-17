package bg.sofia.uni.fmi.mjt.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerTest {

    LoggerOptions options;
    Logger logger;
    Robot wallE;

    LogParser parser;

    @BeforeEach
    public void setUp() {
        options = new LoggerOptions(Robot.class, "logs");
    }

    @Test
    public void testGetLogsNegativeTail() {
        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));
        assertThrows(IllegalArgumentException.class, () -> parser.getLogsTail(-1));
    }

    @Test
    public void testLogsTailZero() {
        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));
        System.out.println(parser.getLogsTail(0));
        //assertTrue(parser.getLogsTail(0).isEmpty());
    }

    @Test
    public void testGetLastNLogs() {
        options.setMaxFileSizeBytes(512);
        options.setMinLogLevel(Level.DEBUG);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");

        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));

        assertEquals(1, parser.getLogsTail(1).size());

        Log currLog = parser.getLogsTail(1).get(0);

        assertSame(currLog.level(), Level.INFO);
        assertTrue(currLog.message().startsWith("Robot Wall-E is created"));

        wallE.work();
        wallE.work();

        List<Log> logs = parser.getLogsTail(10);

        assertEquals(4, logs.size());

        currLog = logs.get(3);

        assertSame(currLog.level(), Level.WARN);
        assertTrue(currLog.message().startsWith("Wall-E needs to recharge"));

        wallE.work();
        wallE.recharge();

        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-1.txt"));
        logs = parser.getLogsTail(10);

        assertEquals(2, logs.size()); // new log file

        currLog = logs.get(0);
        assertSame(currLog.level(), Level.DEBUG);
        assertTrue(currLog.message().startsWith("Wall-E current energy"));

        currLog = logs.get(1);
        assertSame(currLog.level(), Level.INFO);
        assertTrue(currLog.message().startsWith("Wall-E is recharged"));
    }

    @Test
    public void testLogSwapFiles() {
        options.setMinLogLevel(Level.DEBUG);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");

        wallE.recharge();
        for (int i = 0; i < 12; ++i) {
            wallE.work();
            if (i % 5 == 0) {
                wallE.recharge();
            }
        }

        assertEquals("logs-2.txt", wallE.getLogger().getCurrentFilePath().getFileName().toString());
    }

    @Test
    public void testGetLogsNull() {
        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));
        assertThrows(IllegalArgumentException.class, () -> parser.getLogs(LocalDateTime.now(), null));
    }

    @Test
    public void testGetLogsLevelNull() {
        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));
        assertThrows(IllegalArgumentException.class, () -> parser.getLogs(null));
    }

    @Test
    public void testGetLogsLevel() {
        options.setMaxFileSizeBytes(1024);
        options.setMinLogLevel(Level.WARN);
        options.setShouldThrowErrors(true);
        logger = new DefaultLogger(options);

        wallE = new Robot(logger, "Wall-E");

        for (int i = 0; i < 9; ++i) {
            wallE.work();
            if (i % 3 == 0) {
                wallE.recharge();
            }
        }

        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));

        List<Log> infoLogs = parser.getLogs(Level.INFO);
        List<Log> warnLogs = parser.getLogs(Level.WARN);
        List<Log> errLogs = parser.getLogs(Level.ERROR);

        assertEquals(0, infoLogs.size());
        assertEquals(3, warnLogs.size());
        assertEquals(2, errLogs.size());
    }

    @Test
    public void testGetLogsFromTo() throws InterruptedException {
        options.setMaxFileSizeBytes(2048);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");

        wallE.work();
        Thread.sleep(1000);

        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "logs-0.txt"));

        wallE.work();
        List<Log> logs = parser.getLogs(
                LocalDateTime.now().minusSeconds(1),
                LocalDateTime.now()
        );

        assertEquals(2, logs.size());

        logs = parser.getLogs(
                LocalDateTime.now().minusSeconds(3),
                LocalDateTime.now()
        );

        assertEquals(4, logs.size());
    }

    @Test
    public void testLogNullLevel() {
        logger = new DefaultLogger(options);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");
        assertThrows(IllegalArgumentException.class,
                () -> wallE.getLogger().log(null, LocalDateTime.now(), "Hello"));
    }

    @Test
    public void testLogNullTime() {
        logger = new DefaultLogger(options);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");
        assertThrows(IllegalArgumentException.class,
                () -> wallE.getLogger().log(Level.ERROR, null, "Hello"));
    }

    @Test
    public void testLogNullMsg() {
        logger = new DefaultLogger(options);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");
        assertThrows(IllegalArgumentException.class,
                () -> wallE.getLogger().log(Level.WARN, LocalDateTime.now(), null));
    }

    @Test
    public void testLogEmptyMsg() {
        logger = new DefaultLogger(options);
        logger = new DefaultLogger(options);
        wallE = new Robot(logger, "Wall-E");
        assertThrows(IllegalArgumentException.class,
                () -> wallE.getLogger().log(Level.INFO, LocalDateTime.now(), ""));
    }

    @Test
    public void testLogLevel() {
        logger = new DefaultLogger(options);

        assertSame(Level.INFO, options.getMinLogLevel());
        logger.getOptions().setMinLogLevel(Level.DEBUG);
        assertSame(Level.DEBUG, options.getMinLogLevel());
    }

    @Test
    public void parseInvalidLogFile() {
        File f = new File(options.getDirectory() + File.separator + "invalid.txt");
        f.getParentFile().mkdirs();

        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (var bw = Files.newBufferedWriter(f.toPath(), StandardOpenOption.APPEND)) {
            bw.write("Hello Java");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        parser = new DefaultLogParser(Paths.get(options.getDirectory() + File.separator + "invalid.txt"));

        assertThrows(LogException.class, () -> parser.getLogsTail(1));
        options.setShouldThrowErrors(false);
    }

    @AfterEach
    public void clear() {
        deleteLogsRecursively(Paths.get(options.getDirectory()).toAbsolutePath().toFile());
    }

    private void deleteLogsRecursively(File dir) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (final File f : files) {
                deleteLogsRecursively(f);
            }
        }

        dir.delete();
    }
}
