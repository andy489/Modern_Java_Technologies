package bg.sofia.uni.fmi.mjt.io;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ObjectStream {
    public static void main(String... args) {
        Path filePath = Path.of("files", "students.bin");

        Student firstStudent = new Student("Pesho", 20, List.of(5, 4, 6, 5));
        Student secondStudent = new Student("Stamat", 20, List.of(3, 2, 4, 5));

        writeStudentsToFile(filePath, firstStudent, secondStudent);
        readStudentsFromFile(filePath);
    }

    private static void writeStudentsToFile(Path file, Student... students) {
        try (var os = new ObjectOutputStream(Files.newOutputStream(file))) {
            for (Student student : students) {
                os.writeObject(student);
                os.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while writing to file %s.", file.getFileName()));
        }
    }

    public static void readStudentsFromFile(Path file) {
        try (var is = new ObjectInputStream(Files.newInputStream(file))) {

            Object studentObj;
            while ((studentObj = is.readObject()) != null) {
                System.out.println(studentObj);

                Student s = (Student) studentObj;
                System.out.println("Name: " + s.name());
            }
        } catch (EOFException e) {
            // EMPTY BODY
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(String.format("The file %s does not exist", file.getFileName()), e);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while reading from file %s", file.getFileName()), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
