package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

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

    public GameData createGame(String gameName) throws DataAccessException {
        int gameId = generateGameID();
        ChessGame chessGame = new ChessGame();
        String game = gameSerializer.serializeGame(chessGame);
        String createSQL = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) " +
                "VALUES (?,?,?,?,?)";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(createSQL)) {
            prepStatement.setInt(1, gameId);
            prepStatement.setString(2, null);
            prepStatement.setString(3, null);
            prepStatement.setString(4, gameName);
            prepStatement.setString(5, game);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return
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

    private int generateGameID() throws DataAccessException {
        Random random = new Random();
        int ID = 0;
        boolean unique = false;
        while (!unique) {
            ID = 1000 + random.nextInt(9000);
            String checkUnique = "SELECT COUNT(*) FROM games WHERE gameID = ?";
            try (var connection = DatabaseManager.getConnection();
                 var prepStatement = connection.prepareStatement(checkUnique)) {
                prepStatement.setInt(1, ID);
                var results = prepStatement.executeQuery();
                if (results.next() && results.getInt(1) == 0) {
                    unique = true;
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        return ID;
    }
}
