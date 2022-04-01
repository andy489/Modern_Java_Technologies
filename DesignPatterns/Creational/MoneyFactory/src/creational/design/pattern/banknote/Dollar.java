package creational.design.pattern.banknote;

public class Dollar implements Banknote{
    public static final String PRINT = "Printing 100 dollars";

    @Override
    public String print(){
        return PRINT;
    }
}
