package ui;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.AuthData;

public class ServerFacade {

    public static AuthData register(String username, String password, String email) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}",
                                        username, password, email);

        String response = Communicator.post("/user", jsonInput);

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

        String response = Communicator.post("/session", jsonInput);

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
        Communicator.delete("/session", authToken);
    }
}
