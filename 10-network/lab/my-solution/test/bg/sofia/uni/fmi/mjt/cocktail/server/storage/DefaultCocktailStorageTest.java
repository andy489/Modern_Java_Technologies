package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultCocktailStorageTest {
    private CocktailStorage storage;

    private final String testCocktailName1 = "mojito";
    private final String testCocktailName2 = "martini";

    private final String testIngredientName1 = "Vodka";
    private final String testIngredientValue1 = "100ml";

    private final String testIngredientName2 = "Mint";
    private final String testIngredientValue2 = "3_leaves";

    private final Ingredient testIngredient1 = Ingredient.of(testIngredientName1, testIngredientValue1);
    private final Ingredient testIngredient2 = Ingredient.of(testIngredientName2, testIngredientValue2);

    private final Cocktail testCocktail1 = Cocktail.of(testCocktailName1, Set.of(testIngredient1, testIngredient2));
    private final Cocktail testCocktail2 = Cocktail.of(testCocktailName2, Set.of(testIngredient1));

    @BeforeEach
    public void setUp() {
        storage = new DefaultCocktailStorage();
    }

    @Test
    public void testCreateExistingCocktail() throws CocktailAlreadyExistsException {
        storage.createCocktail(testCocktail1);

        assertThrows(CocktailAlreadyExistsException.class, () -> storage.createCocktail(testCocktail1));
    }

    @Test
    public void testGetCocktailsNoCocktails() {
        assertTrue(storage.getCocktails().isEmpty());
    }

    @Test
    public void testGetCocktails() throws CocktailAlreadyExistsException {
        assertEquals(0, storage.getCocktails().size());

        storage.createCocktail(testCocktail1);
        assertEquals(1, storage.getCocktails().size());

        storage.createCocktail(testCocktail2);
        assertEquals(2, storage.getCocktails().size());
    }

    @Test
    public void testGetCocktailByNameNoName() {
        assertThrows(CocktailNotFoundException.class, () -> storage.getCocktail(testCocktail2.name()));
    }

    @Test
    public void testGetCocktailByName() throws CocktailAlreadyExistsException, CocktailNotFoundException {
        storage.createCocktail(testCocktail1);
        assertEquals(testCocktail1, storage.getCocktail(testCocktail1.name()));
    }

    @Test
    public void testGetCocktailByIngredientNoIngredient() throws CocktailAlreadyExistsException {
        assertTrue(storage.getCocktailsWithIngredient("Whiskey").isEmpty());
    }

    @Test
    public void testGetCocktailByIngredient() throws CocktailAlreadyExistsException {
        storage.createCocktail(testCocktail1);
        assertEquals(List.of(testCocktail1), storage.getCocktailsWithIngredient("Vodka"));

        storage.createCocktail(testCocktail2);
        assertEquals(2, storage.getCocktailsWithIngredient("Vodka").size());
    }
}
