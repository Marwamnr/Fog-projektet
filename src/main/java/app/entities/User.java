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
}