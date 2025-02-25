package dataaccess;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    private List<UserData> users = new ArrayList<>();
    private List<AuthData> auths = new ArrayList<>();

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.getUsername() == username) {
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData userData) {
        users.add(userData);
    }

    public void createAuth(AuthData authData) {
        auths.add(authData);
    }
}
