package dataaccess;

import chess.ChessGame;
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
        GameData gameData = new GameData(gameID, "", "", gameName, game);
        games.add(gameData);
        return gameData;
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
}
