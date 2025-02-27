package server;

import spark.*;
import dataaccess.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        UserDataAccess dataAccess = new UserDataAccess();
        UserService userService = new UserService(dataAccess);
        UserHandler userHandler = new UserHandler(userService);
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", userHandler.register);


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
