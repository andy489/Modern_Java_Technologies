package bg.sofia.uni.fmi.mjt.smartfridge.recipe;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.Set;

public class Pancakes implements Recipe {

    @Override
    public Set<Ingredient<? extends Storable>> getIngredients() {
        return null;
    }

    @Override
    public void addIngredient(Ingredient<? extends Storable> ingredient) {
    }

    @Override
    public void removeIngredient(String itemName) {
    }
}
