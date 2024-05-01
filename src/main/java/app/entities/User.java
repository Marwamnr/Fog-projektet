package app.entities;

public class User {
    private int UserId;
    private String email;
    private String password;
    private String role;

    public User(int UserId, String email, String password, String role) {
        this.UserId = UserId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return UserId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
