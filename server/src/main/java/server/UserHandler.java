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

    public Object register(Request req, Response res) {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        if (userData == null || userData.username() == null || userData.password()== null || userData.email() == null) {
            return ResponseHandler.handleResponse(res, 400, "bad request");
        }
        try {
            AuthData authData = userService.register(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e){
            if (e.getMessage().equals("already taken")) {
                return ResponseHandler.handleResponse(res, 403, e.getMessage());
            } else {
                return ResponseHandler.handleResponse(res, 500, e.getMessage());
            }
        }
    }

    public Object login(Request req, Response res) {
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        try {
            AuthData authData = userService.login(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e) {
            return UnauthorizedHandler.unauthorizedError(e, res);
        }
    }

    public Object logout(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            userService.logout(authToken);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            return UnauthorizedHandler.unauthorizedError(e, res);
        }
    }

}
