package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class AuthSQLTests {

    AuthData defaultAuth;
    AuthSQLDAO dataAccess;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        dataAccess = new AuthSQLDAO();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auths")) {
                statement.executeUpdate();
            }
        }

        defaultAuth = new AuthData("token", "user1");


    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("TRUNCATE auths")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void successfulCreate() throws DataAccessException, SQLException {
        dataAccess.createAuth(defaultAuth);
        String resultToken;
        String resultUser;
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(
                     "SELECT authToken, username FROM auths WHERE authToken=?")) {
            prepStatement.setString(1, defaultAuth.authToken());
            try (var results = prepStatement.executeQuery()) {
                results.next();
                resultToken = results.getString("authToken");
                resultUser = results.getString("username");
            }
        }

        Assertions.assertEquals(defaultAuth, new AuthData(resultToken, resultUser));
    }

    @Test
    void tryToAddSameAuth() throws DataAccessException, SQLException {
        dataAccess.createAuth(defaultAuth);
        Assertions.assertThrows(RuntimeException.class, () -> dataAccess.createAuth(defaultAuth));
    }
}
