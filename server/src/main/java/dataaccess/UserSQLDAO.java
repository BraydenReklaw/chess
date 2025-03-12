package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class UserSQLDAO {

    public UserSQLDAO() {
        createUsersTable();
    }

    private void createUsersTable() {
        String usersTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "username varchar(255) NOT NULL, " +
                "password varchar(255) NOT NULL, " +
                "email varchar(255), " +
                "PRIMARY KEY (username))";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(usersTableSQL);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        String createSQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(createSQL)) {
            prepStatement.setString(1, userData.username());
            prepStatement.setString(2, userData.password());
            prepStatement.setString(3, userData.email());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        String selectSQL = "SELECT * FROM users WHERE username = ?";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(selectSQL)){
            prepStatement.setString(1, username);
            try (var results = prepStatement.executeQuery()) {
                if (results.next()) {
                    return new UserData(results.getString("username"), results.getString("password"), results.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }
}
