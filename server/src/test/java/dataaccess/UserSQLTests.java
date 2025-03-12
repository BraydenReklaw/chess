package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class UserSQLTests {

    UserSQLDAO dataAccess;
    UserData defaultUser;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        dataAccess = new UserSQLDAO();
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        }

        defaultUser = new UserData("user1", "password1", "email1");


    }

    @AfterEach
    void cleanUp() throws SQLException, DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var statement = connection.prepareStatement("TRUNCATE users")) {
                statement.executeUpdate();
            }
        }
    }

    @Test
    void successfulCreate() throws DataAccessException, SQLException {
        dataAccess.createUser(defaultUser);
        String resultUser;
        String resultPassword;
        String resultEmail;
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE username=?")) {
            prepStatement.setString(1, defaultUser.username());
            try (var results = prepStatement.executeQuery()) {
                results.next();
                resultUser = results.getString("username");
                resultPassword = results.getString("password");
                resultEmail = results.getString("email");
            }
        }

        Assertions.assertEquals(defaultUser.username(), resultUser);
        Assertions.assertEquals(defaultUser.password(), resultPassword);
        Assertions.assertEquals(defaultUser.email(), resultEmail);
    }

    @Test
    void tryCreateSameUser() throws DataAccessException {
        dataAccess.createUser(defaultUser);
        Assertions.assertThrows(DataAccessException.class, () -> dataAccess.createUser(defaultUser));
    }

    @Test
    void successfulGet() throws DataAccessException {
        dataAccess.createUser(defaultUser);
        UserData result = dataAccess.getUser("user1");
        Assertions.assertEquals(result, defaultUser);
    }

    @Test
    void getBadUser() throws DataAccessException {
        dataAccess.createUser(defaultUser);
        Assertions.assertNull(dataAccess.getUser("user2"));
    }

    @Test
    void clear() throws DataAccessException {
        dataAccess.createUser(defaultUser);
        dataAccess.createUser(new UserData("user2", "password2", "email"));
        dataAccess.createUser(new UserData("user3", "password3", "email"));

        dataAccess.clearAll();

        Assertions.assertTrue(dataAccess.isEmpty());
    }
}
