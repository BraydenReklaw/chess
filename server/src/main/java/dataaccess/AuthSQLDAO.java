package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class AuthSQLDAO {

    public AuthSQLDAO() throws SQLException, DataAccessException {
        createTable();
    }

    private void createTable() throws SQLException, DataAccessException {
        String authTableSQL = "CREATE TABLE IF NOT EXISTS auths (" +
                "authToken varchar(255) PRIMARY KEY, " +
                "username varchar(255) NOT NULL)";
        try (var connection = DatabaseManager.getConnection(); var statement = connection.createStatement()) {
            statement.execute(authTableSQL);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAuth(AuthData authData) {
        String createSQL = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try
    }
}
