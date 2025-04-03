package server;

import com.google.gson.Gson;
import dataaccess.GameSession;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class SocketHandler {

    private static Map<Integer, GameSession> gameSessions = new HashMap<>();
    private static Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);

//            default:
//                sendError(session, "Invalid command type");
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException {
        GameSession gameSession = gameSessions.computeIfAbsent(command.getGameID(), k -> new GameSession());
        gameSession.addClient(session, command.getAuthToken());
        sendLoadGame(session);
        sendNotification(gameSession, command.getAuthToken() + " connected to game");
    }

    private void sendLoadGame(Session session) throws IOException {
        ServerMessage loadGameMessage =  new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        session.getRemote().sendString(gson.toJson(loadGameMessage));
    }

    private void sendNotification(GameSession gameSession, String message) throws IOException {
        ServerMessage notificationMessage =  new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notificationMessage.setMessage(message);
        for (Session clientSession : gameSession.getClients()) {
            clientSession.getRemote().sendString(gson.toJson(notificationMessage));
        }
    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        error.setMessage(errorMessage);
    }
}
