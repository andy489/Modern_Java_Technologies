package creational.design.pattern.banknote;

public class Ruble implements Banknote{
    public static final String PRINT= "Printing 5000 rubles";

    @Override
    public String print() {
        return PRINT;
    }
}
