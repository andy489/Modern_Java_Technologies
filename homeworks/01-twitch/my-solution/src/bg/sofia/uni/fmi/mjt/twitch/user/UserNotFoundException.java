package bg.sofia.uni.fmi.mjt.twitch.user;

public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
