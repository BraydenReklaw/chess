package server;

import spark.*;
import dataaccess.*;
import service.*;

public class Server {
    UserDataAccess userDataAccess = new UserDataAccess();
    AuthDataAccess authDataAccess = new AuthDataAccess();
    GameDataAccess gameDataAccess = new GameDataAccess();
    UserService userService = new UserService(userDataAccess, authDataAccess);
    GameService gameService = new GameService(gameDataAccess, authDataAccess);
    ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
    UserHandler userHandler = new UserHandler(userService);
    GameHandler gameHandler = new GameHandler(gameService);
    ClearHandler clearHandler = new ClearHandler(clearService);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (request, response) -> userHandler.register(request, response));
        Spark.delete("/db", (request, response) -> clearHandler.clear(request, response));
        Spark.post("/session", (request, response) -> userHandler.login(request, response));
        Spark.delete("/session", (request, response) -> userHandler.logout(request, response));
        Spark.get("/game", (request, response) -> gameHandler.list(request, response));
        Spark.post("/game", (request, response) -> gameHandler.create(request, response));
        Spark.put("/game", (request, response) -> gameHandler.join(request, response));

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
