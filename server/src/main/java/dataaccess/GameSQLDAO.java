package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GameSQLDAO {
    private GameSerialization gameSerializer;

    public GameSQLDAO() {
        this.gameSerializer = new GameSerialization();
        createGamesTable();
    }

    private void createGamesTable() {
        String gamesTableSQL = "CREATE TABLE IF NOT EXISTS games (" +
                "gameID INT NOT NULL, " +
                "whiteUsername VARCHAR(255), " +
                "blackUsername VARCHAR(255), " +
                "gameName VARCHAR(255)," +
                "game TEXT " +
                "PRIMARY KEY (gameID))";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(gamesTableSQL);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<GameData> listAll() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        String listSQL = "SELECT * FROM games";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(listSQL);
             var results = prepStatement.executeQuery()) {
            while (results.next()) {
                int gameID = results.getInt("gameID");
                String whiteUsername = results.getString("whiteUsername");
                String blackUsername = results.getString("blackUsername");
                String gameName = results.getString("gameName");
                ChessGame game = gameSerializer.deserializeGame(results.getString("game"));

                GameData chessGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                games.add(chessGame);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }
}
