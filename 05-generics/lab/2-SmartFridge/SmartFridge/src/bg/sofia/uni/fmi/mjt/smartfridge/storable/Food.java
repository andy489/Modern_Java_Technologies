package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;

public class Food extends AbstractStorable{
    public Food(String name, LocalDate expirationDate){
        super(name, expirationDate);
    }

    public Food(String name){
        super(name, LocalDate.MAX);
    }

    @Override
    public StorableType getType() {
        return StorableType.FOOD;
    }
}
