package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private GameDataAccess gameDataAccess;
    private AuthSQLDAO authDataAccess;

    public GameService(GameDataAccess gameDataAccess, AuthSQLDAO authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void checkAuth(AuthData authData) throws DataAccessException {
        if (authData == null) {
            throw new DataAccessException("unauthorized");
        }
    }

    public Collection<GameData> list(String authToken) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(authToken);
        checkAuth(authData);
        return gameDataAccess.listAll();
    }

    public String create(String authToken, String gameName) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(authToken);
        checkAuth(authData);
        GameData gameData = gameDataAccess.createGame(gameName);
        return String.valueOf(gameData.gameID());
    }

    public void join(String authToken, GameData gameData, String playerColor) throws DataAccessException {

        // Look into passing only 1 argument to updateGame, creating a new GameData instance with desired updates
        // and passing that to updateGame

        AuthData authData = authDataAccess.getAuth(authToken);
        checkAuth(authData);
        GameData game = gameDataAccess.getGame(gameData.gameID());
        if (game == null) {
            throw new DataAccessException("no game");
        }
        if (gameData.whiteUsername() == null && playerColor.equals("WHITE")) {
            gameDataAccess.updateGame(authData, playerColor, game);
        } else if (gameData.blackUsername() == null && playerColor.equals("BLACK")) {
            gameDataAccess.updateGame(authData, playerColor, game);
        } else {
            throw new DataAccessException("taken");
        }
    }
}
