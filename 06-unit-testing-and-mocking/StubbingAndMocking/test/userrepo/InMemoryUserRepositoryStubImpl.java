package userrepo;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepositoryStubImpl implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean exist(String email) {
        return users.containsKey(email);
    }

    @Override
    public void save(User user) {
        users.put(user.getEmail(), user);
    }
}
