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
        UserData user = dataAccess.getUser(userData.getUsername());
        if (user != null) {
            throw new DataAccessException("User Already Exists");
        }
        dataAccess.createUser(userData);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.getUsername());
        dataAccess.createAuth(authData);

        return authData;
    }
}
