package io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadFromByteArray {
    public static void main(String... args) throws IOException {
//        InputStream in = new ByteArrayInputStream("Ivan / Спасов".getBytes());
        Reader in = new InputStreamReader(new ByteArrayInputStream("Ivan / Спасов".getBytes())); // UTF-8 by default
        /*
            read() reads the next byte of data from the input stream.
            The value byte is returned as an int in the range 0 to 255.
            If no byte is available because the end of the stream
            has been reached, the value -1 is returned
        */

        int current;
        while ((current = in.read()) != -1) {
            System.out.printf("%c", current);
        }

        in.close();

        // output: Ivan / Ð¡Ð¿Ð°ÑÐ¾Ð²
    }
}
