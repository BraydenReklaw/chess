package server;

import com.google.gson.Gson;
import spark.Route;
import spark.Request;
import spark.Response;


public class UserHandler {
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Route register = (Request req, Response res) -> {
        UserRequest request = new Gson().fromJson(req.body(), UserRequest.class);
//      success case
        UserResponse response = userService.register(request);
        res.status(200);
        return new Gson().toJson(response);
    };
}
