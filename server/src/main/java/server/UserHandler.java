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
        if (userData == null || userData.username() == null || userData.password()== null || userData.email() == null) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try {
            AuthData authData = userService.register(userData);
            res.status(200);
            return new Gson().toJson(authData);
        } catch (DataAccessException e){
            if (e.getMessage().equals("already taken")) {
                res.status(403);
//                return new Gson().toJson(new DataAccessException("Error: already taken"));
                return "{ \"message\": \"Error: already taken\" }";
            } else {
                res.status(500);
                return new Gson().toJson(new DataAccessException("Error: " + e.getMessage()));
            }
        }
    }
}
