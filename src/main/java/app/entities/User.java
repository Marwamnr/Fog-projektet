package app.entities;

import java.util.Objects;

public class User {
    private int UserId;
    private String email;
    private String password;
    private String roles;
    private String adress;
    private String phonenumber;

    public User(int UserId, String email, String password, String roles, String adress, String phonenumber) {
        this.UserId = UserId;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.adress = adress;
        this.phonenumber = phonenumber;
    }

    // for integration test
    public User(String email, String password, String roles, String adress, String phonenumber) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.adress = adress;
        this.phonenumber = phonenumber;
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

    public String getRoles() {
        return roles;
    }

    public String getAdress() {
        return adress;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    // for integration test
    public void setUserId(int userId) {
        UserId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (UserId != user.UserId) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(roles, user.roles)) return false;
        if (!Objects.equals(adress, user.adress)) return false;
        return Objects.equals(phonenumber, user.phonenumber);
    }

    @Override
    public int hashCode() {
        int result = UserId;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (adress != null ? adress.hashCode() : 0);
        result = 31 * result + (phonenumber != null ? phonenumber.hashCode() : 0);
        return result;
    }
}