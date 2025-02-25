package server;

public class UserRequest {
    private String username;
    private String password;
    private String email;

    public UserRequest() {
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }
}
