package creational.design.pattern.factory;

import creational.design.pattern.banknote.Banknote;
import creational.design.pattern.banknote.BanknoteType;
import creational.design.pattern.banknote.Dollar;
import creational.design.pattern.banknote.Euro;
import creational.design.pattern.banknote.Ruble;

public class MoneyFactory {
    public static Banknote of(BanknoteType type){
        return switch(type){
            case DOLLAR -> new Dollar();
            case EURO -> new Euro();
            case RUBLE -> new Ruble();
        };
    }
}
