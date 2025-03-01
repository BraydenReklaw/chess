package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class AuthDataAccess {
    private List<AuthData> auths = new ArrayList<>();

    public AuthDataAccess() {
        this.auths = auths;
    }

    public void createAuth(AuthData authData) {
        auths.add(authData);
    }

    public void clearAll() {
        auths.clear();
    }
}
