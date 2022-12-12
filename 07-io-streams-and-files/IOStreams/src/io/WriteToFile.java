package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteToFile {
    public static void main(String... args) throws IOException {
        boolean append = false;
        OutputStream os = new FileOutputStream("./files/test.txt", append);

        os.write("FOO BAR BAZ".getBytes());
        os.flush();
        os.close();
    }
}
