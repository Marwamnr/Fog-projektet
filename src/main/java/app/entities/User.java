package app.entities;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (UserId != user.UserId) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!roles.equals(user.roles)) return false;
        if (!adress.equals(user.adress)) return false;
        return phonenumber.equals(user.phonenumber);
    }

    @Override
    public int hashCode() {
        int result = UserId;
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + roles.hashCode();
        result = 31 * result + adress.hashCode();
        result = 31 * result + phonenumber.hashCode();
        return result;
    }
}