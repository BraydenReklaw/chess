package dataaccess;

public class DatabaseInit {
    public static void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
