package bg.sofia.uni.fmi.mjt.twitch.user;

public class DefaultUser implements User {
    String userName;
    UserStatus userStatus;

    public DefaultUser(String userName, UserStatus userStatus) {
        this.userName = userName;
        this.userStatus = userStatus;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public UserStatus getStatus() {
        return userStatus;
    }

    @Override
    public void setStatus(UserStatus status) {
        userStatus = status;
    }
}
