package userrepo;

public interface UserRepository {
    boolean exist(String name);

    void save(User user);
}
