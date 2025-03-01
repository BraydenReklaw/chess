package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import service.GameService;
import spark.*;

import java.util.Collection;

public class GameHandler {
    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object list(Request req, Response res) {
        String authToken = req.headers("authorization");
        try {
            Collection<GameData> games = gameService.list(authToken);
            res.status(200);
            return new Gson().toJson(games);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("unauthorized")) {
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else {
                res.status(500);
                return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
            }
        }
    }
}
