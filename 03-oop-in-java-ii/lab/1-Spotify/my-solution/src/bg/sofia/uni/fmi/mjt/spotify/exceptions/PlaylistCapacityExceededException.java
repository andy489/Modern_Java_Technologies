package bg.sofia.uni.fmi.mjt.spotify.exceptions;

public class PlaylistCapacityExceededException extends Throwable {
    public PlaylistCapacityExceededException(String msg) {
        super(msg);
    }
}
