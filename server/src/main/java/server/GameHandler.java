package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public Object join(Request req, Response res) {
        String authToken = req.headers("authorization");
        JsonObject jsonObject = JsonParser.parseString(req.body()).getAsJsonObject();
        String playerColor = jsonObject.has("playerColor") ?
                jsonObject.get("playerColor").getAsString() : null;
        jsonObject.remove("playerColor");
        GameData gameData = new Gson().fromJson(req.body(), GameData.class);
        Collection<String> playerColors = new ArrayList<>();
        playerColors.add("WHITE");
        playerColors.add("BLACK");
        if (playerColor == null || playerColor.isEmpty()) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } else if (String.valueOf(gameData.gameID()) == null) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        } else if (!playerColors.contains(playerColor)) {
            res.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try {
            gameService.join(authToken, gameData, playerColor);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            if (e.getMessage().equals("unauthorized")) {
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            } else if (e.getMessage().equals("taken")){
                res.status(403);
                return "{ \"message\": \"Error: already taken\" }";
            } else if (e.getMessage().equals("no game")) {
                res.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            } else {
                res.status(500);
                return "{ \"message\": \"Error: " + e.getMessage() + "\" }";
            }
        }
    }
}
