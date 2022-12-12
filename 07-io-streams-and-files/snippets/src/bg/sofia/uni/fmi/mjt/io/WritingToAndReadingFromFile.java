package bg.sofia.uni.fmi.mjt.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WritingToAndReadingFromFile {
    public static void main(String... args) {
        Path filePath = Path.of("files", "writingAndReadingFromFile.txt");
        String text = "Write this string to my file" + System.lineSeparator();

        writeToFile(filePath, text);
        readFromFile(filePath);
    }

    private static void writeToFile(Path filePath, String text) {
        File file = new File(filePath.getParent().toString());
        boolean creation = file.mkdirs();

        try (var bw = Files.newBufferedWriter(filePath)) {
            bw.write(text);
            bw.flush();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while writing to file %s.", filePath.getFileName()));
        }
    }

    private static void readFromFile(Path filePath) {
        try (var br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while reading from file %s", filePath.getFileName()));
        }
    }
}
