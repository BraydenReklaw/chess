package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public Collection<GameData> list(String authToken) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
        return gameDataAccess.listAll();
    }
}
