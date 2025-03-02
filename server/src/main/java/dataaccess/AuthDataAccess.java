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

    public AuthData getAuth(String authToken) {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(String authToken) {
        auths.removeIf(auth -> auth.authToken().equals(authToken));
    }

    public void clearAll() {
        auths.clear();
    }

    public boolean isEmpty() {
        return auths.isEmpty();
    }
}
