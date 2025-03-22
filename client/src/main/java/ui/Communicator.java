package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Communicator {
    private static String ServerURL = "http://localhost:8810";

    public static String post(String endpoint, String jsonInput) throws IOException {
        BufferedReader reader;
        StringBuilder response = new StringBuilder();

        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            requestBody.write(input, 0, input.length);
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
        else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            String responseLine;
            while ((responseLine = reader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    // this is to facilitate ServerFacadeTests database cleanup
    public static void delete(String endpoint) throws IOException {
        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.connect();
        connection.getResponseCode();
    }
}
