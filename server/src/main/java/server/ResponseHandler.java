package server;

import spark.Response;

public class ResponseHandler {
    public static String handleResponse(Response res, int statusCode, String message) {
        res.status(statusCode);
        return "{ \"message\": \"Error: " + message + "\" }";
    }
}
