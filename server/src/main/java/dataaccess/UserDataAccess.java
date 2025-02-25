package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class UserDataAccess {
    private List<UserData> users = new ArrayList<>();
    private List<AuthData> auths = new ArrayList<>();

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username() == username) {
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
