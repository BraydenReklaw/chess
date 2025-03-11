package service;

import dataaccess.*;

public class ClearService {
    private UserDataAccess userData;
    private AuthSQLDAO authData;
    private GameDataAccess gameData;

    public ClearService(UserDataAccess userData, AuthSQLDAO authData, GameDataAccess gameData) {
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