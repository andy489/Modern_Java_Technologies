package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadFromFile {
    private static char toUpper(int c) {
        return (c >= 97 && c <= 122) ? (char) (c - 32) : (char) c;
    }

    public static void main(String... args) throws IOException {
        InputStream fileInputStream = new FileInputStream("./files/a.txt");

        int data;
        while ((data = fileInputStream.read()) != -1) {
            System.out.printf("%c ", toUpper(data));
        }

        fileInputStream.close();
    }
}
