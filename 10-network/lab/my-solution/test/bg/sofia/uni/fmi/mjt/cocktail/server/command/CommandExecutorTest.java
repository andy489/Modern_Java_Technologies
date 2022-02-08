package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandExecutorTest {
    private CocktailStorage storage;
    private CommandExecutor cmdExecutor;

    private Gson gson;

    private final String testCocktailName = "mojito";

    private final String testIngredientName1 = "Vodka";
    private final String testIngredientValue1 = "100ml";

    private final String testIngredientName2 = "Mint";
    private final String testIngredientValue2 = "3_leaves";

    private final Ingredient testIngredient1 = Ingredient.of(testIngredientName1, testIngredientValue1);
    private final Ingredient testIngredient2 = Ingredient.of(testIngredientName2, testIngredientValue2);

    private final Cocktail testCocktail1 = Cocktail.of(testCocktailName, Set.of(testIngredient1, testIngredient2));

    @BeforeEach
    public void setUp() {
        storage = mock(CocktailStorage.class);
        cmdExecutor = new CommandExecutor(storage);
        gson = new Gson();
    }

    @Test
    public void testExecuteWrongCommand() {
        String[] args = {};
        assertEquals("Unknown command", cmdExecutor.execute(new Command("test", args)));
    }

    @Test
    public void testExecuteCreate() throws CocktailAlreadyExistsException {
        String[] empty = {};
        String[] args = {testCocktailName, testIngredientName1 + "=" + testIngredientValue1};

        cmdExecutor.execute(new Command("create", empty));
        verify(storage, never()).createCocktail(Cocktail.of(testCocktailName,
                Set.of(Ingredient.of(testIngredientName1, testIngredientValue1))));

        assertEquals("{\"status\":\"CREATED\"}", cmdExecutor.execute(new Command("create", args)));
        verify(storage, times(1))
                .createCocktail(Cocktail.of(testCocktailName,
                        Set.of(Ingredient.of(testIngredientName1, testIngredientValue1))));
    }

    @Test
    public void testExecuteGetAll() {
        String[] empty = {};
        String[] args = {"all"};

        cmdExecutor.execute(new Command("get", empty));
        verify(storage, never()).getCocktails();

        cmdExecutor.execute(new Command("get", args));
        verify(storage, times(1)).getCocktails();
    }

    @Test
    void testExecuteGetAllByName() throws CocktailNotFoundException {
        String[] args = {"by-name", "mojito"};

        when(storage.getCocktail(testCocktailName)).thenReturn(testCocktail1);

        assertEquals("{\"status\":\"OK\",\"cocktails\":" + gson.toJson(testCocktail1) + "}",
                cmdExecutor.execute(new Command("get", args)));
    }

    @Test
    void testExecuteGetAllByIngredientNoIngredient() {
        String[] args = {"by-ingredient", "cherry"};

        when(storage.getCocktailsWithIngredient("cherry")).thenReturn(Collections.emptyList());

        assertEquals("{\"status\":\"OK\",\"cocktails\":[]}",
                cmdExecutor.execute(new Command("get", args)));
    }

    @Test
    void testExecuteGetAllByIngredient() {
        String[] args = {"by-ingredient", "mint"};

        when(storage.getCocktailsWithIngredient("mint")).thenReturn(Collections.singleton(testCocktail1));

        assertEquals("{\"status\":\"OK\",\"cocktails\":[" + gson.toJson(testCocktail1) + "]}",
                cmdExecutor.execute(new Command("get", args)));
    }

    @Test
    void testExecuteCommandWithMissingArgument() {
        String[] args1 = {"all"};
        String[] args2 = {"by-name"};
        String[] args3 = {"by-ingredient"};

        assertEquals("{\"status\":\"OK\",\"cocktails\":[]}",
                cmdExecutor.execute(new Command("get", args1)));
        assertTrue(cmdExecutor.execute(new Command("get", args2)).startsWith("Invalid cnt of args"));
        assertTrue(cmdExecutor.execute(new Command("get", args3)).startsWith("Invalid cnt of args"));
    }

    @Test
    void testExecuteCommandWithMoreArgumentThanNeeded() {
        String[] args = {"all one two"};

        assertEquals("Unknown command", cmdExecutor.execute(new Command("get", args)));
    }
}
