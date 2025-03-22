package ui;

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerFacade {

    public static String register(String username, String password, String email) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}",
                                        username, password, email);

        String response = Communicator.post("/user", jsonInput);

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("username")) {
            return "Registered";
        } else {
            String message = jsonResponse.get("message").getAsString();
            return message;
        }
    }

    public static String logIn(String username, String password) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        String response = Communicator.post("/session", jsonInput);

        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();

        if (jsonResponse.has("username")) {
            return "Logged in";
        } else {
            String message = jsonResponse.get("message").getAsString();
            return message;
        }
    }
}
