package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class AuthSQLDAO {

    public AuthSQLDAO() {
        createAuthsTable();
    }

    private void createAuthsTable() {
        String authTableSQL = "CREATE TABLE IF NOT EXISTS auths (" +
                "authToken varchar(255) PRIMARY KEY, " +
                "username varchar(255) NOT NULL)";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(authTableSQL);
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        String createSQL = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(createSQL)) {
            prepStatement.setString(1, authData.authToken());
            prepStatement.setString(2, authData.username());
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String selectSQL = "SELECT * FROM auths WHERE authToken = ?";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(selectSQL)){
            prepStatement.setString(1, authToken);
            try (var results = prepStatement.executeQuery()) {
                if (results.next()) {
                    return new AuthData(results.getString("authToken"), results.getString("username"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String deleteSQL = "DELETE FROM auths WHERE authToken = ?";
        try (var connection = DatabaseManager.getConnection();
             var prepStatement = connection.prepareStatement(deleteSQL)) {
            prepStatement.setString(1, authToken);
            prepStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clearAll() throws DataAccessException {
        String clearSQL = "TRUNCATE auths";
        try (var connection = DatabaseManager.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(clearSQL);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    // This is here to service tests in ServiceTests
    public boolean isEmpty() throws DataAccessException {
        String countSQL = "SELECT COUNT(*) AS total FROM auths";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(countSQL)) {
            if (rs.next()) {
                return rs.getInt("total") == 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }
}
