package server;

import com.google.gson.Gson;
import spark.Route;
import spark.Request;
import spark.Response;
import dataaccess.*;


public class UserHandler {
    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Route register = (Request req, Response res) -> {
        try {
            UserRequest request = new Gson().fromJson(req.body(), UserRequest.class);
            UserResponse response = userService.register(request);
            res.status(200);
            return new Gson().toJson(response);
        } catch (DataAccessException e) {
//            Only a placeholder
            return null;
        }
//        catch (DataAccessException e) {
//            res.status(400);
//            return new Gson().toJson(new ErrorResponse(e.getMessage()));
//        } catch (Exception e) {
//            res.status(500);
//            return new Gson().toJson(new ErrorResponse("Internal Server Error"));
//        }
    };
}
