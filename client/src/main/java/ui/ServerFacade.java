package ui;

import java.io.IOException;

public class ServerFacade {

    public static String register(String username, String password, String email) throws IOException {
        String jsonInput = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}", username, password);
        return Communicator.post("/user", jsonInput);
    }
}
