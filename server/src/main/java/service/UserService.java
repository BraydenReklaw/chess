package service;

import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {
    private UserDataAccess dataAccess;

    public UserService(UserDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        UserData user = dataAccess.getUser(userData.username());
        if (user != null) {
            throw new DataAccessException("already taken");
        }
        dataAccess.createUser(userData);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        dataAccess.createAuth(authData);

        return authData;
    }
}
