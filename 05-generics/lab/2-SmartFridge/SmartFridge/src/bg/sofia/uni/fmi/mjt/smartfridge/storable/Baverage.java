package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;

public class Baverage extends AbstractStorable {
    public Baverage(String name, LocalDate expirationDate) {
        super(name, expirationDate);
    }

    public Baverage(String name) {
        super(name, LocalDate.MAX);
    }

    @Override
    public StorableType getType() {
        return StorableType.BEVERAGE;
    }
}
