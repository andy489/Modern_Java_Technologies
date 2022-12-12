package io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadPrimaryDataFromFile {
    public static void main(String... args) throws IOException {
        int idA = 1;
        String nameA = "City";
        int populationA = 5;
        float tempA = 1.0f;

        int idB = 2;
        String nameB = "S";
        int populationB = 2;
        float tempB = 1.45f;

        DataOutputStream os = new DataOutputStream(new FileOutputStream("./files/binary.data"));

        os.writeInt(idA);
        os.writeUTF(nameA);
        os.writeInt(populationA);
        os.writeFloat(tempA);

        os.writeInt(idB);
        os.writeUTF(nameB);
        os.writeInt(populationB);
        os.writeFloat(tempB);

        os.flush();
        os.close();

        FileInputStream fis = new FileInputStream("./files/binary.data");
        DataInputStream dis = new DataInputStream(fis);

        int cityId = dis.readInt();
        System.out.println("City Id: " + cityId);
        String cityName = dis.readUTF();
        System.out.println("City Name: " + cityName);
        int cityPopulation = dis.readInt();
        System.out.println("City Population: " + cityPopulation);
        float cityTemperature = dis.readFloat();
        System.out.println("City Temperature: " + cityTemperature);
    }
}
