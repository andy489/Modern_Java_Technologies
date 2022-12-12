package files;

import java.io.File;
import java.nio.file.Path; // Since Java 11
import java.nio.file.Paths; // Newer

public class PathsExamples {
    public static void main(String... args) {
        Path pathToUserHomeDir = Path.of("C:\\Users\\joe"); // C:\Users\joe
        Path pathToAFile = Path.of("C:\\Users\\joe\\orders.txt"); // C:\Users\joe\orders.txt
        Path relPathToAFile = Path.of("Users", "joe", "orders.txt"); // \Users\joe\orders.txt

        Path linuxPathToAFile = Paths.get("/home", "joe", "file.txt"); // /home/joe/file.txt
        Path linuxRelativePath = Paths.get("documents", "FileIO.odp"); // documents/FileIO.odp

        System.out.printf("Current system file separator: %s%s", File.separator, System.lineSeparator());

        System.out.println(linuxRelativePath.toAbsolutePath()); // /Users/andreystoev/Desktop/IOStreams/documents/FileIO.odp
        // toRealPath - Absolut without symlinks, . and ..
        Path p1 = Paths.get("/home/joe/foo");
        Path p2 = p1.resolve("bar/baz");

        System.out.println(p2);
    }
}
