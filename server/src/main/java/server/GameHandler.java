package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import model.UserData;
import service.GameService;
import spark.*;

import java.util.*;

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
            // The Json wants an object, not an array
            Map<String, Collection<GameData>> gameMap = new HashMap<>();
            gameMap.put("games", games);
            return new Gson().toJson(gameMap);
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

    public Object create(Request req, Response res) {
        String authToken = req.headers("authorization");
        GameData gameData = new Gson().fromJson(req.body(), GameData.class);
        String gameName = gameData.gameName();
        if (gameName == null) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try {
            String gameID = gameService.create(authToken, gameName);
            res.status(200);
            Map<String, String> wrappedGameID = new HashMap<>();
            wrappedGameID.put("gameID", gameID);
            return new Gson().toJson(wrappedGameID);
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
