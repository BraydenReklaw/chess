package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.*;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;


public class ServerFacade {

    public static int port;
    private static SocketCommunicator socket;

    public ServerFacade(int port) {
        ServerFacade.port = port;
    }

    public static AuthData register(String username, String password, String email) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}",
                                        username, password, email);

        String response = Communicator.post("/user", jsonInput, null, String.valueOf(port));

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("username")) {
            String user = jsonResponse.get("username").getAsString();
            String auth = jsonResponse.get("authToken").getAsString();
            return new AuthData(auth, user);
        } else {
            return null;
        }
    }

    public static AuthData logIn(String username, String password) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        String response = Communicator.post("/session", jsonInput, null, String.valueOf(port));

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("username")) {
            String user = jsonResponse.get("username").getAsString();
            String auth = jsonResponse.get("authToken").getAsString();
            return new AuthData(auth, user);
        } else {
            return null;
        }
    }

    public static void logOut(String authToken) throws IOException {
        Communicator.delete("/session", authToken, String.valueOf(port));
    }

    public static Collection<GameData> listGames(String authToken) throws IOException {
        String response = Communicator.get("/game", authToken, String.valueOf(port));

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (!jsonResponse.has("games")) {
            return null;
        }
        JsonArray gamesArray = jsonResponse.getAsJsonArray("games");
        Collection<GameData> games = new ArrayList<>();

        for (JsonElement game : gamesArray) {
            GameData gameData = new Gson().fromJson(game, GameData.class);
            games.add(gameData);
        }

        return games;
    }

    public static String createGame(String authToken, String gameName) throws IOException {
        String jsonInput = String.format("{\"gameName\": \"%s\"}", gameName);

        String response = Communicator.post("/game", jsonInput, authToken, String.valueOf(port));

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("message")) {
            return "Error";
        }
        return null;
    }

    public static String joinGame(String authToken, String color, int gameID) throws IOException {
        String jsonInput = String.format("{\"playerColor\": \"%s\", \"gameID\": %d}", color, gameID);

        String response = Communicator.put("/game", jsonInput, authToken, String.valueOf(port));

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("message")) {
            return "Error";
        }
        return null;
    }

    public void socketConnect() {
        try {
            socket = new SocketCommunicator(port);
        }
        catch (Exception e) {
            System.out.println("Failed to make connection with server");
        }
    }

    public static void sendCommand(UserGameCommand command) throws IOException {
        socket.sendMessage(command);
    }

    // This is purely for database clearing
    public static void testDelete(String endpoint, int port) throws IOException {
        Communicator.testDelete(endpoint, String.valueOf(port));
    }
}
