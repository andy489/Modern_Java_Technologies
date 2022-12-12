package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int age;
    private String name;

    Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Person{age:%d, Name:%s}", age, name);
    }
}

public class ObjectWritesToFile {
    public static void main(String... args) throws IOException, ClassNotFoundException {
        Person p = new Person(33, "Andy");

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("./files/person.dat"));

        os.writeObject(p);
        os.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("./files/person.dat"));
        Person pp = (Person) in.readObject();

        System.out.println(pp.toString());

        in.close();
    }
}
