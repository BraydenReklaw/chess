package server;

import dataaccess.DataAccessException;
import spark.Response;

public class UnauthorizedHandler {
    public static Object unauthorizedError(DataAccessException e, Response res) {
        if (e.getMessage().equals("unauthorized")) {
            return ResponseHandler.handleResponse(res, 401, e.getMessage());
        } else {
            return ResponseHandler.handleResponse(res, 500, e.getMessage());
        }
    }
}
