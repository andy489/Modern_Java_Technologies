import java.util.ArrayList;
import java.util.List;

class LivingThing {
}

class Human extends LivingThing {
    private String name;

    public Human(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }
}

class Student extends Human {
    private int fn;

    public Student(String name, int fn) {
        super(name);
        this.fn = fn;
    }

    @Override
    public String toString() {
        return "Student{" +
                "fn=" + fn +
                '}';
    }
}

class FMIStudent extends Student {
    public FMIStudent(String name, int fn) {
        super(name, fn);
    }
}

public class GenericPlayground {

    // The get & Put principle in action
    private static void getHumans(List<? extends Human> listOfHumans) {

        // we can safely get, and we can rely on always getting a Human
        for (Human h : listOfHumans) { // we can safely iterate as Human and call methods of Human
            System.out.println(h.name());
        }

        // we can only put null
        listOfHumans.add(null);
        // listOfHumans.add(new Human("Pesho", 123)); // Will not compile:
        // not every Human is a Student (and we can call the method with ListOfStudents)
    }

    private static void putHumans(List<? super Human> listOfSuperHumans) {
        // listOfSuperHumans can be listOfLivingThings, listOfObjects... in the method call

        // we can safely put instances of Human and successors of Human
        listOfSuperHumans.add(new Student("Pesho Peshev", 61234));
        listOfSuperHumans.add(new Human("Maria Lilova"));
        listOfSuperHumans.add(new FMIStudent("Maya Litova", 66712));
        // listOfSuperHumans.add(new LivingThing()); // Will not compile

        // if we get, we can just rely on getting a java.lang.Object
        Object o = listOfSuperHumans.get(0);

        if (o instanceof Student s) {
            System.out.println(s.name());
        }

        System.out.println(o);
    }

    public static int neitherGetNorPut(List<?> listOfUnknown) {
        // if we get, we can just rely on getting a java.lang.Object
        Object o = listOfUnknown.get(0);

        // we can only put null
        listOfUnknown.add(null);

        // listOfUnknown.add("Some String"); // Will not compile:
        // Method can be called with listOfStudents, and what Student is "Some String"?

        // we can use only methods that are agnostic to the type of element
        return listOfUnknown.size();
    }

    public static void main(String... args) {
        List<FMIStudent> listOfFMIStudents = new ArrayList<>();
        List<Student> listOfStudents = new ArrayList<>();
        List<Human> listOfHumans = new ArrayList<>();
        List<LivingThing> listOfLivingThings = new ArrayList<>();
        List<Object> listOfObjects = new ArrayList<>();

        getHumans(listOfHumans);
        getHumans(listOfStudents);
        getHumans(listOfFMIStudents);

        putHumans(listOfHumans);
        putHumans(listOfLivingThings);
        putHumans(listOfObjects);

        System.out.println(neitherGetNorPut(new ArrayList<>(List.of(1, 2, 3)))); // 4, we add null in the method body
    }
}
