package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {
    private GameSQLDAO gameDataAccess;
    private AuthSQLDAO authDataAccess;

    public GameService(GameSQLDAO gameDataAccess, AuthSQLDAO authDataAccess) {
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
        if (game.whiteUsername() == null && playerColor.equals("WHITE")) {
            GameData updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(), game.gameName(), game.game());
            gameDataAccess.updateGame(updatedGame);
        } else if (game.blackUsername() == null && playerColor.equals("BLACK")) {
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(), game.gameName(), game.game());
            gameDataAccess.updateGame(updatedGame);
        } else {
            throw new DataAccessException("taken");
        }
    }
}
