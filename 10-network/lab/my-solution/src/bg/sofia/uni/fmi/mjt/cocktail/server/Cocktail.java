package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.Set;

public record Cocktail(String name, Set<Ingredient> ingredients) {
    public static Cocktail of(String name, Set<Ingredient> ingredients) {
        return new Cocktail(name, ingredients);
    }
}
