package initializers;

class InitializersDemoBase {
    int x;
    static int y;

    private static final double MY_CONST;

    {
        x = 1;
        System.out.println("Parent initializer executed");
    }

    static {
        y = 1;
        System.out.println("Parent static initializer executed");
        MY_CONST = 3.14;
    }

    public InitializersDemoBase() {
        System.out.println("Parent constructor executed");
    }
}

public class InitializersDemo extends InitializersDemoBase {
    private int a;
    private static int b;

    {
        a = 5;
        System.out.println("Child initializer 1 executed");
    }

    static {
        b = 7;
        System.out.println("Child static initializer executed");
    }

    public InitializersDemo() {
        super();
        a = 10;
        b = 10;
        System.out.println("Child constructor executed");
    }

    {
        a = 16;
        System.out.println("Child initializer 2 executed");
    }

    public static void main(String... args) {
        InitializersDemo initDemo = new InitializersDemo();
    }
}
