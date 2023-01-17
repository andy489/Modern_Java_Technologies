package bg.sofia.uni.fmi.mjt.smartfridge.ingredient;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.Objects;

public record DefaultIngredient<E extends Storable>(E item, int quantity) implements Ingredient<E> {
    public DefaultIngredient {
        if (item == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Ingredient cannot have negative or 0 quantity");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && getClass() != o.getClass()) return false;
        DefaultIngredient<Storable> that = (DefaultIngredient) o;
        return Objects.equals(item.getName(), that.item().getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(item.getName());
    }
}