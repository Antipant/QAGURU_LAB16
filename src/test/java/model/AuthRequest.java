package model;

public class AuthRequest {
    public String email;
    public String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public AuthRequest(String email) {
        this.email = email;
    }
}
