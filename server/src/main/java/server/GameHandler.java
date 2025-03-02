package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.DataAccessException;
import model.GameData;
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
                return ResponseHandler.handleResponse(res, 401, e.getMessage());
            } else {
                return ResponseHandler.handleResponse(res, 500, e.getMessage());
            }
        }
    }

    public Object create(Request req, Response res) {
        String authToken = req.headers("authorization");
        GameData gameData = new Gson().fromJson(req.body(), GameData.class);
        String gameName = gameData.gameName();
        if (gameName == null) {
            return ResponseHandler.handleResponse(res, 400, "bad request");
        }
        try {
            String gameID = gameService.create(authToken, gameName);
            res.status(200);
            Map<String, String> wrappedGameID = new HashMap<>();
            wrappedGameID.put("gameID", gameID);
            return new Gson().toJson(wrappedGameID);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("unauthorized")) {
                return ResponseHandler.handleResponse(res, 401, e.getMessage());
            } else {
                return ResponseHandler.handleResponse(res, 500, e.getMessage());
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
            return ResponseHandler.handleResponse(res, 400, "bad request");
        } else if (String.valueOf(gameData.gameID()) == null) {
            return ResponseHandler.handleResponse(res, 400, "bad request");
        } else if (!playerColors.contains(playerColor)) {
            return ResponseHandler.handleResponse(res, 400, "bad request");
        }
        try {
            gameService.join(authToken, gameData, playerColor);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            switch (e.getMessage()) {
                case "unauthorized": return ResponseHandler.handleResponse(res, 401, e.getMessage());
                case "taken": return ResponseHandler.handleResponse(res, 403, "already taken");
                case "no game": return ResponseHandler.handleResponse(res, 400, "bad request");
                default: return ResponseHandler.handleResponse(res, 500, e.getMessage());
            }
        }
    }
}
