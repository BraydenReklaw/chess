package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class GameSQLTests {

    GameSQLDAO dataAccess;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        dataAccess = new GameSQLDAO();
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        }
    }

    @AfterEach
    void cleanUp() throws SQLException, DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void successfulCreate() throws DataAccessException {
        GameData results = dataAccess.createGame("game1");
        Assertions.assertInstanceOf(Integer.class, results.gameID());
        Assertions.assertNull(results.whiteUsername());
        Assertions.assertNull(results.blackUsername());
        Assertions.assertEquals("game1", results.gameName());
        Assertions.assertInstanceOf(ChessGame.class, results.game());
    }

    @Test
    void badGameName() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createGame(null));
    }

    @Test
    void successfulList() throws DataAccessException {
        dataAccess.createGame("game1");
        dataAccess.createGame("game2");
        dataAccess.createGame("game3");

        Assertions.assertEquals(3, dataAccess.listAll().size());
    }

    @Test
    void listWhenNoGames() throws DataAccessException {
        Assertions.assertEquals(0, dataAccess.listAll().size());
    }

    @Test
    void successfulGet() throws DataAccessException {
        GameData createdGame = dataAccess.createGame("game1");
        GameData result = dataAccess.getGame(createdGame.gameID());
        Assertions.assertEquals(createdGame.gameID(), result.gameID());
        Assertions.assertEquals(createdGame.whiteUsername(), result.whiteUsername());
        Assertions.assertEquals(createdGame.blackUsername(), result.blackUsername());
        Assertions.assertEquals(createdGame.gameName(), result.gameName());
        Assertions.assertEquals(createdGame.game().getBoard(), result.game().getBoard());
    }

    @Test
    void badID() throws DataAccessException {
        dataAccess.createGame("game1");
        Assertions.assertNull(dataAccess.getGame(1));
    }

    @Test
    void SuccessfulUpdate() throws DataAccessException {
        GameData createdGame = dataAccess.createGame("game1");
        dataAccess.updateGame(new AuthData("token", "user1"), "WHITE", createdGame);
        Assertions.assertEquals("user1", dataAccess.getGame(createdGame.gameID()).whiteUsername());
    }

    @Test
    void updateNonexistentGame() {
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.updateGame(
                new AuthData("token", "user1"),
                "WHITE",
                new GameData(1, null, null, null, null)));
    }
}
