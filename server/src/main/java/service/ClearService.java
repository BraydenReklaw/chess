package service;

import dataaccess.*;

public class ClearService {
    private UserSQLDAO userData;
    private AuthSQLDAO authData;
    private GameSQLDAO gameData;

    public ClearService(UserSQLDAO userData, AuthSQLDAO authData, GameSQLDAO gameData) {
        this.userData = userData;
        this.authData = authData;
        this.gameData = gameData;
    }

    public void clear() throws DataAccessException {
        userData.clearAll();
        authData.clearAll();
        gameData.clearAll();
    }
}