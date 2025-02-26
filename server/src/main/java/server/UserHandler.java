package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import spark.*;
import service.*;
import model.*;

public class UserHandler {
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws DataAccessException {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        AuthData authData = userService.register(userData);
        res.status(200);
        return new Gson().toJson(authData);
    }
}
