package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Communicator {
    private static String ServerURL = "http://localhost:8810";

    public static String post(String endpoint, String jsonInput, String token) throws IOException {
        BufferedReader reader;
        StringBuilder response = new StringBuilder();

        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("POST");
        if (token != null) {
            connection.setRequestProperty("authorization", token);
        }
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

    public static String delete(String endpoint, String jsonInput) throws IOException {
        BufferedReader reader;
        StringBuilder response = new StringBuilder();

        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("authorization", jsonInput);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
        }
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    public static String get(String endpoint, String token) throws IOException {
        BufferedReader reader;
        StringBuilder response = new StringBuilder();

        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("authorization", token);

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
        }
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    public static String put(String endpoint, String jsonInput, String Token) throws IOException {
        BufferedReader reader;
        StringBuilder response = new StringBuilder();

        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("authorization", Token);
        connection.setDoOutput(true);

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            requestBody.write(input, 0, input.length);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
        }
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    // this is to facilitate ServerFacadeTests database cleanup
    public static void testDelete(String endpoint) throws IOException {
        URL url = new URL(ServerURL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("DELETE");
        connection.connect();
        connection.getResponseCode();
    }
}
