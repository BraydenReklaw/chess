package service;

import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {
    private UserDataAccess dataAccess;
    private AuthDataAccess authAccess;

    public UserService(UserDataAccess dataAccess, AuthDataAccess authAccess) {
        this.dataAccess = dataAccess;
        this.authAccess = authAccess;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        UserData user = dataAccess.getUser(userData.username());
        if (user != null) {
            throw new DataAccessException("already taken");
        }
        dataAccess.createUser(userData);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authAccess.createAuth(authData);

        return authData;
    }

    public AuthData login(UserData userData) throws DataAccessException {
        UserData user = dataAccess.getUser(userData.username());
        if (user == null || !user.password().equals(userData.password())) {
            throw new DataAccessException("unauthorized");
        }

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authAccess.createAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authAccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
        authAccess.deleteAuth(authToken);
    }
}
