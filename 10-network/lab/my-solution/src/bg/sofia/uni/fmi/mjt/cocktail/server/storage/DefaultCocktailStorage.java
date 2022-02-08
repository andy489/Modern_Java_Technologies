package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCocktailStorage implements CocktailStorage {
    private final Map<String, Cocktail> nameCocktail;
    private final List<Cocktail> cocktails;
    private final Map<String, List<Cocktail>> ingredientCocktails;

    public DefaultCocktailStorage() {
        cocktails = new ArrayList<>();
        nameCocktail = new HashMap<>();
        ingredientCocktails = new HashMap<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (nameCocktail.containsKey(cocktail.name())) {
            throw new CocktailAlreadyExistsException("Cocktail already exists.");
        }

        nameCocktail.put(cocktail.name(), cocktail);
        cocktails.add(cocktail);

        for (Ingredient ing : cocktail.ingredients()) {
            if (ingredientCocktails.containsKey(ing.name())) {
                ingredientCocktails.get(ing.name()).add(cocktail);
            } else {
                ingredientCocktails.put(ing.name(), new ArrayList<>(Collections.singleton(cocktail)));
            }
        }
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktails;
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        if (!ingredientCocktails.containsKey(ingredientName)) {
            return Collections.emptyList();
        }

        return ingredientCocktails.get(ingredientName);
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        if (!nameCocktail.containsKey(name)) {
            throw new CocktailNotFoundException("No such cocktail, yet.");
        }
        return nameCocktail.get(name);
    }
}
