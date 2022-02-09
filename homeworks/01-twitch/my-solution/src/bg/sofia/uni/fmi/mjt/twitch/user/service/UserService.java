package bg.sofia.uni.fmi.mjt.twitch.user.service;

import java.util.Map;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

public interface UserService {

    /**
     * Returns a map of users, where the key is the username.
     *
     * @return a map of users, where the key is the username
     */
    Map<String, User> getUsers();

}
