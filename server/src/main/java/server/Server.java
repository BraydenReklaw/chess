package server;

import spark.*;
import dataaccess.*;
import service.*;

public class Server {
    UserDataAccess userDataAccess = new UserDataAccess();
    AuthDataAccess authDataAccess = new AuthDataAccess();
    UserService userService = new UserService(userDataAccess, authDataAccess);
    ClearService clearService = new ClearService(userDataAccess, authDataAccess);
    UserHandler userHandler = new UserHandler(userService);
    ClearHandler clearHandler = new ClearHandler(clearService);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandler::register);
        Spark.delete("/db", (request, response) -> clearHandler.clear(request, response));

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
