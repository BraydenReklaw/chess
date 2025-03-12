package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.*;

public class GameDataAccess {
    private List<GameData> games = new ArrayList<>();

    public GameDataAccess() {
        this.games = games;
    }

    public Collection<GameData> listAll() {
        return games;
    }

    public void clearAll() {
        games.clear();
    }

    public GameData createGame(String gameName) {
        int gameID = generateGameID();
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        games.add(gameData);
        return gameData;
    }

    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public void updateGame(AuthData authData, String playerColor, GameData gameData) throws DataAccessException {
        if (playerColor.equals("WHITE")) {
            GameData game = getGame(gameData.gameID());
            if (game.whiteUsername() != null) {
                throw new DataAccessException("taken");
            }
            GameData updatedGame = new GameData(game.gameID(), authData.username(), game.blackUsername(),
                    game.gameName(), game.game());
            games.remove(game);
            games.add(updatedGame);
        } else {
            GameData game = getGame(gameData.gameID());
            if (game.blackUsername() != null) {
                throw new DataAccessException("taken");
            }
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), authData.username(),
                    game.gameName(), game.game());
            games.remove(game);
            games.add(updatedGame);
        }
    }

    public int generateGameID() {
        boolean unique = true;
        while (true) {
            Random random = new Random();
            int randomInt = 1000 + random.nextInt(9000);
            for (GameData game : games) {
                if (game.gameID() == randomInt) {
                    unique = false;
                }
            }
            if (unique) {
                return randomInt;
            }
        }

    }

    public boolean isEmpty() {
        return games.isEmpty();
    }
}
