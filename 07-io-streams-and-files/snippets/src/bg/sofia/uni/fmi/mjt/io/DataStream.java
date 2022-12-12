package bg.sofia.uni.fmi.mjt.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStream {
    public static void main(String... args) {
        writeWithDataStream();
        readDataStream();
    }

    /**
     * Writes structured data using DataOutputStream
     * https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/DataOutputStream.html
     */
    private static void writeWithDataStream() {
        try (var dos = new DataOutputStream(new FileOutputStream("files/test.dat"))) {
            dos.writeInt(16);
            dos.writeFloat(3.14f);
            dos.writeDouble(2.71828182);
            dos.writeUTF("utf");
            dos.flush();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while writing to file %s.", "test.dat"));
        }
    }

    /**
     * Reads structured data using DataInputStream
     * https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/io/DataInputStream.html
     */
    private static void readDataStream() {
        try (var dis = new DataInputStream(new FileInputStream("files/test.dat"))) {
            System.out.println("int: " + dis.readInt());
            System.out.println("float: " + dis.readFloat());
            System.out.println("double: " + dis.readDouble());
            System.out.println("string: " + dis.readUTF());
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("A problem occurred while reading from file %s.", "test.dat"));
        }
    }
}
