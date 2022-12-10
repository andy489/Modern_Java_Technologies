package userrepo;

public class PositiveUserRepositoryStubImpl implements UserRepository{
    @Override
    public boolean exist(String name) {
        return true;
    }

    @Override
    public void save(User user) {

    }
}
