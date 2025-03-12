package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private UserDataAccess dataAccess;
    private AuthSQLDAO authAccess;

    public UserService(UserDataAccess dataAccess, AuthSQLDAO authAccess) {
        this.dataAccess = dataAccess;
        this.authAccess = authAccess;
    }

    public AuthData createAuthData(UserData userData) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        authAccess.createAuth(authData);
        return authData;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        UserData user = dataAccess.getUser(userData.username());
        if (user != null) {
            throw new DataAccessException("already taken");
        }

        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        dataAccess.createUser(new UserData(userData.username(), hashedPassword, userData.email()));

        return createAuthData(userData);
    }

    public AuthData login(UserData userData) throws DataAccessException {
        UserData user = dataAccess.getUser(userData.username());
        if (user == null || !BCrypt.checkpw(userData.password(), user.password())) {
            throw new DataAccessException("unauthorized");
        }

        return createAuthData(user);
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = authAccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
        authAccess.deleteAuth(authToken);
    }
}
