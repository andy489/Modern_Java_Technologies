package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.Objects;

public abstract class AbstractStorable implements Storable {
    private final String name;
    private final LocalDate expirationDate;

    public AbstractStorable(String name, LocalDate expirationDate) {
        this.name = name;
        this.expirationDate = expirationDate;
    }

    @Override
    public abstract StorableType getType();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public LocalDate getExpiration() {
        return expirationDate;
    }

    @Override
    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractStorable that = (AbstractStorable) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
