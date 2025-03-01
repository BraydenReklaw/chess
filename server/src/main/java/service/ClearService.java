package service;

import dataaccess.*;

public class ClearService {
    private UserDataAccess userData;
    private AuthDataAccess authData;
    private GameDataAccess gameData;

    public ClearService(UserDataAccess userData, AuthDataAccess authData, GameDataAccess gameData) {
        this.userData = userData;
        this.authData = authData;
        this.gameData = gameData;
    }

    public void clear() {
        userData.clearAll();
        authData.clearAll();
        gameData.clearAll();
    }
}