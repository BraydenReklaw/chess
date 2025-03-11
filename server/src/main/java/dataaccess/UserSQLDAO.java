package dataaccess;

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
}
