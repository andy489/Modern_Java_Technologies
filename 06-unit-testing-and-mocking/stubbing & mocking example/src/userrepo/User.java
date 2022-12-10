package userrepo;

public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && getClass() != o.getClass()) return false;

        User that = (User) o;
        return this.email.equals(that.email);
    }
}
