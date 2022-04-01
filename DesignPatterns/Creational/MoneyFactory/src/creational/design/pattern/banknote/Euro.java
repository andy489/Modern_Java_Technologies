package creational.design.pattern.banknote;

public class Euro implements Banknote{
    public static final String PRINT= "Printing 100 euro";

    @Override
    public String print() {
        return PRINT;
    }
}
