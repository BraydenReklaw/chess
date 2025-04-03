package server;

import spark.*;
import dataaccess.*;
import service.*;

public class Server {
    UserSQLDAO userDataAccess;
    AuthSQLDAO authDataAccess;
    GameSQLDAO gameDataAccess;
    UserService userService;
    GameService gameService;
    ClearService clearService;
    UserHandler userHandler;
    GameHandler gameHandler;
    ClearHandler clearHandler;

    public Server() {
        try {
            DatabaseInit.initialize();

            userDataAccess = new UserSQLDAO();
            authDataAccess = new AuthSQLDAO();
            gameDataAccess = new GameSQLDAO();
            userService = new UserService(userDataAccess, authDataAccess);
            gameService = new GameService(gameDataAccess, authDataAccess);
            clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
            userHandler = new UserHandler(userService);
            gameHandler = new GameHandler(gameService);
            clearHandler = new ClearHandler(clearService);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize the server due to database issues: " + e.getMessage());
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", SocketHandler.class);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> userHandler.register(request, response));
        Spark.delete("/db", (request, response) -> clearHandler.clear(request, response));
        Spark.post("/session", (request, response) -> userHandler.login(request, response));
        Spark.delete("/session", (request, response) -> userHandler.logout(request, response));
        Spark.get("/game", (request, response) -> gameHandler.list(request, response));
        Spark.post("/game", (request, response) -> gameHandler.create(request, response));
        Spark.put("/game", (request, response) -> gameHandler.join(request, response));


        //This line initializes the server and can be removed once you have a functioning endpoint
        // Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
