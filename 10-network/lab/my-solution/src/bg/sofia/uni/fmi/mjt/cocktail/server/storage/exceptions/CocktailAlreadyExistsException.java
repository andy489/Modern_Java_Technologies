package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions;

public class CocktailAlreadyExistsException extends Throwable {
    public CocktailAlreadyExistsException(String msg) {
        super(msg);
    }
}
