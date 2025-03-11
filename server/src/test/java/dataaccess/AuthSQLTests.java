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
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE auths")) {
                statement.executeUpdate();
            }
        }

        defaultAuth = new AuthData("token", "user1");


    }

    @AfterEach
    void tearDown() throws SQLException, DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE auths")) {
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
                     "SELECT * FROM auths WHERE authToken=?")) {
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
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createAuth(defaultAuth));
    }

    @Test
    void successfulGet() throws DataAccessException {
        dataAccess.createAuth(defaultAuth);
        AuthData result = dataAccess.getAuth("token");
        Assertions.assertEquals(result, defaultAuth);
    }

    @Test
    void tryGetBadToken() throws DataAccessException {
        dataAccess.createAuth(defaultAuth);
        Assertions.assertNull(dataAccess.getAuth("badToken"));
    }

    @Test
    void successfulDelete() throws DataAccessException {
        dataAccess.createAuth(defaultAuth);
        dataAccess.deleteAuth("token");
        Assertions.assertNull(dataAccess.getAuth("token"));
    }

    @Test
    void tryDeleteNonexistentAuth()  throws DataAccessException {
        // Don't really know to implement this test, as deleteAuth would ultimately change nothing and throw nothing if
        // given a bad token, and logic in the services prevent a bad token reaching this function at all.
        Assertions.assertDoesNotThrow(() -> dataAccess.deleteAuth("Token"));
    }
}
