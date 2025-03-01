package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;

public class GameService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
}
