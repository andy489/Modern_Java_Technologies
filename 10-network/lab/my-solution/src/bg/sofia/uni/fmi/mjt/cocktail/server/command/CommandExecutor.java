package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.CocktailStorage;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandExecutor {
    private static final String INVALID_ARGS_CNT_MSG_FMT_CREATE =
            "Invalid cnt of args: \"%s\" expects at least %d args. Usage: \"%s\"";

    private static final String INVALID_ARGS_CNT_MSG_FMT_GET =
            "Invalid cnt of args: \"%s\" expects %d args. Usage: \"%s\"";

    private static final String ALREADY_EXISTS_MSG =
            "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s already exists\"}";

    private static final String NOT_FOUND_MSG =
            "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail %s not found\"}";

    private static final String OK_CREATED = "{\"status\":\"CREATED\"}";
    private static final String OK_COCKTAILS = "{\"status\":\"OK\",\"cocktails\":%s}";

    private static final String CREATE = "create";
    private static final String GET = "get";

    private static final String ALL = "all";
    private static final String BY_NAME = "by-name";
    private static final String BY_INGREDIENT = "by-ingredient";

    private static final String UNKNOWN = "Unknown command";
    private static final String EMPTY = "[]";

    private final CocktailStorage storage;
    private final Gson gson;

    public CommandExecutor(CocktailStorage storage) {
        this.storage = storage;
        gson = new Gson();
    }

    public String execute(Command cmd) {
        return switch (cmd.command()) {
            case CREATE -> createCocktail(cmd.arguments());
            case GET -> getStatistics(cmd.arguments());
            default -> "Unknown command";
        };
    }

    private String createCocktail(String[] args) {
        if (args.length < 2) {
            return String.format(INVALID_ARGS_CNT_MSG_FMT_CREATE, CREATE, 2, CREATE + "<ingredient>=<quantity>");
        }

        Set<Ingredient> listIngredients = new HashSet<>();

        String nameCocktail = args[0];

        for (int i = 1; i < args.length; ++i) {
            String[] nameAmount = args[i].split("=");
            listIngredients.add(Ingredient.of(nameAmount[0], nameAmount[1]));
        }

        try {
            storage.createCocktail(Cocktail.of(nameCocktail, listIngredients));
        } catch (CocktailAlreadyExistsException e) {
            return String.format(ALREADY_EXISTS_MSG, nameCocktail);
        }

        return OK_CREATED;
    }

    private String getStatistics(String[] args) {
        if (args.length == 0) {
            return UNKNOWN;
        } else {
            String additionalArgument = args[0];
            switch (additionalArgument) {
                case ALL -> {
                    if (args.length == 1) {
                        return String.format(OK_COCKTAILS,
                                gson.toJson(storage.getCocktails()));
                    } else {
                        return String.format(INVALID_ARGS_CNT_MSG_FMT_GET, GET, 1, GET + " " + ALL);
                    }
                }
                case BY_NAME -> {
                    if (args.length == 2) {
                        String cocktailName = args[1];
                        try {
                            return String.format(OK_COCKTAILS,
                                    gson.toJson(storage.getCocktail(cocktailName)));
                        } catch (CocktailNotFoundException e) {
                            return String.format(NOT_FOUND_MSG, cocktailName);
                        }
                    } else {
                        return String.format(INVALID_ARGS_CNT_MSG_FMT_GET,
                                GET, 2, GET + " " + BY_NAME + " <cocktail_name>");
                    }
                }
                case BY_INGREDIENT -> {
                    if (args.length == 2) {
                        String ingredientName = args[1];

                        Collection<Cocktail> byIng = storage.getCocktailsWithIngredient(ingredientName);

                        if (byIng.isEmpty()) {
                            return String.format(OK_COCKTAILS, EMPTY);
                        }

                        return String.format(OK_COCKTAILS,
                                gson.toJson(storage.getCocktailsWithIngredient(ingredientName)));
                    } else {
                        return String.format(INVALID_ARGS_CNT_MSG_FMT_GET,
                                GET, 2, GET + " " + BY_INGREDIENT + " <ingredient_name>");
                    }
                }

                default -> {
                    return UNKNOWN;
                }
            }
        }
    }
}
