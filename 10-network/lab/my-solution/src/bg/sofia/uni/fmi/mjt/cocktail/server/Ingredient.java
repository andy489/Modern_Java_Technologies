package bg.sofia.uni.fmi.mjt.cocktail.server;

public record Ingredient(String name, String amount) {
    public static Ingredient of(String name, String amount) {
        return new Ingredient(name, amount);
    }
}