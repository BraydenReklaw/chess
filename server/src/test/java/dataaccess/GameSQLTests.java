package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class GameSQLTests {

    GameSQLDAO dataAccess;
    GameData defaultChessGame;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        dataAccess = new GameSQLDAO();
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE games")) {
                statement.executeUpdate();
            }
        }
        ChessGame defaultGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        defaultGame.setBoard(board);

        defaultChessGame = new GameData(1234, "white",
                "black", "default", defaultGame);
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
        Assertions.assertEquals(results.gameName(), "game1");
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
}
