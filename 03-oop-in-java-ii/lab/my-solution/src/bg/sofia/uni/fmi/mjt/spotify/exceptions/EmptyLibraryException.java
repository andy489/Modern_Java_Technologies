package bg.sofia.uni.fmi.mjt.spotify.exceptions;

public class EmptyLibraryException extends Throwable {
    public EmptyLibraryException() {
        super("The library is empty. There is nothing to erase from it.");
    }
}
