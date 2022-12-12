package bg.sofia.uni.fmi.mjt.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExecutionTimeComparator {
    private static final int MAX_ITERATIONS = 1_000_000;
    private static final int NANO_SECONDS_IN_ONE_MILLIS = 1_000_000;
    private static final int BUFFER_SIZE = 8192;

    public static void main(String... args) {
        System.err.println("Read time comparison results:");

        Path filePath = Path.of("files", "compareExecutionTime.txt");
        String line = "Hello World";

        fillFileWithText(filePath, line);

        System.out.println("Single byte read took: " + readFromFile(filePath) + " milliseconds");
        System.out.println("BufferedReader read took: " + readFromFileWithBufferedReader(filePath) + " milliseconds");
        System.out.println("BufferedInputStream read took: " + readFromFileWithBufferedInputStream(filePath) + " milliseconds");
    }

    private static void fillFileWithText(Path file, String line) {
        try (var wr = Files.newBufferedWriter(file)) {
            for (int i = 0; i < MAX_ITERATIONS; i++) {
                wr.write(line);
            }

            wr.flush();

            long writtenMegabytesLength = line.getBytes().length;
            long writtenBytesLength = writtenMegabytesLength * MAX_ITERATIONS;

            System.out.println("Wrote " + writtenBytesLength + " bytes (" + writtenMegabytesLength + " megabytes)" +
                    " to the file" + System.lineSeparator());
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing to a file", e);
        }
    }

    private static long readFromFile(Path file) {
        try (var is = Files.newInputStream(file)) {
            long startTime = System.nanoTime();

            while (is.read() != -1) {
                // Do some processing
            }
            long endTime = System.nanoTime();

            return (endTime - startTime) / NANO_SECONDS_IN_ONE_MILLIS; // milliseconds
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while reading from file %s", file.getFileName()), e);
        }
    }

    private static long readFromFileWithBufferedInputStream(Path file) {
        try (var is = Files.newInputStream(file)) {
            byte[] buff = new byte[BUFFER_SIZE]; // 8K bytes buffer
            int r = 0;
            long startTime = System.nanoTime();

            while ((r = is.read(buff)) != -1) {
            }
            long endTime = System.nanoTime();

            return (endTime - startTime) / NANO_SECONDS_IN_ONE_MILLIS;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while reading from file %s", file.getFileName()), e);
        }
    }

    private static long readFromFileWithBufferedReader(Path file) {
        try (var br = Files.newBufferedReader(file)) {

            long startTime = System.nanoTime();
            while (br.readLine() != null) {
                // Do some processing
            }
            long endTime = System.nanoTime();

            return (endTime - startTime) / NANO_SECONDS_IN_ONE_MILLIS;
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while reading from file %s", file.getFileName()), e);
        }
    }
}
