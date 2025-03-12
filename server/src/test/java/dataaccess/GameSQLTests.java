package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
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
    void successfulList() throws DataAccessException {

    }
}
