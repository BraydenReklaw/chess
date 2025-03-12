package dataaccess;

import java.sql.SQLException;

public class GameSQLDAO {

    public GameSQLDAO() {
        createGamesTable();
    }

    private void createGamesTable() {
        String gamesTableSQL = "CREATE TABLE IF NOT EXISTS games (" +
                "gameID INT NOT NULL, " +
                "whiteUsername VARCHAR(255), " +
                "blackUsername VARCHAR(255), " +
                "gameName VARCHAR(255)," +
                "chessGame TEXT " +
                "PRIMARY KEY (gameID)";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(gamesTableSQL);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
