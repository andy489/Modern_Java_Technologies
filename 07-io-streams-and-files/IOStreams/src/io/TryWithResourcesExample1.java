package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TryWithResourcesExample1 {
    public static void main(String... args) throws IOException {

        String path = "./files/a.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(String.format("Missing file %s", path));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("~All IO streams are closed.");
        }
    }
}
