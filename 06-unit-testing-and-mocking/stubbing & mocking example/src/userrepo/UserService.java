package userrepo;

public class UserService {
    private UserRepository repo;

    public UserService(UserRepository userRepository) {
        this.repo = userRepository;
    }

    public User register(String email, String password) {
        if (repo.exist(email)) {
            throw new UserAlreadyExistException("Failed to save user");
        }

        User user = new User(email, password);
        repo.save(user);
        return user;
    }
}
