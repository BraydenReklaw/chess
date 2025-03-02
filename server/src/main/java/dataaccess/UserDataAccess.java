package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    private List<UserData> users = new ArrayList<>();

    public UserDataAccess() {
        this.users = users;
    }

    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData userData) {
        users.add(userData);
    }

    public void clearAll() {
        users.clear();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }
}
