package server;

import com.google.gson.Gson;
import dataaccess.AuthSQLDAO;
import dataaccess.DataAccessException;
import dataaccess.GameSQLDAO;
import dataaccess.GameSession;
import model.AuthData;
import model.GameData;
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

    private Map<Integer, GameSession> gameSessions = new HashMap<>();
    private Gson gson = new Gson();
    private GameSQLDAO gameDataAccess = new GameSQLDAO();
    private AuthSQLDAO authAccess = new AuthSQLDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);

//            default:
//                sendError(session, "Invalid command type");
        }
    }

    private void handleConnect(Session session, UserGameCommand command) throws IOException, DataAccessException {
        GameSession gameSession = gameSessions.computeIfAbsent(command.getGameID(), k -> new GameSession());
        gameSession.addClient(session, command.getAuthToken());
        GameData game = gameDataAccess.getGame(command.getGameID());
        if (game == null) {
            sendError(session, "Game not found for gameID: " + command.getGameID());
            return;
        }
        AuthData user = authAccess.getAuth(command.getAuthToken());
        if (user == null) {
            sendError(session, "Unauthorized");
            return;
        }
        sendLoadGame(session, game);
        sendNotificationOthers(gameSession, session,  user.username() + " connected to game");
    }

    private void sendLoadGame(Session session, GameData game) throws IOException {
        ServerMessage loadGameMessage =  new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        loadGameMessage.setGame(game);
        session.getRemote().sendString(gson.toJson(loadGameMessage));
    }

    private void sendNotificationOthers(GameSession gameSession, Session root, String message) throws IOException {
        ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notificationMessage.setMessage(message);
        for (Session clientSession : gameSession.getClients()) {
            if (!clientSession.equals(root)) {
                clientSession.getRemote().sendString(gson.toJson(notificationMessage));
            }
        }
    }

    private void sendNotificationAll(GameSession gameSession, String message) throws IOException {
        ServerMessage notificationMessage =  new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notificationMessage.setMessage(message);
        for (Session clientSession : gameSession.getClients()) {
            clientSession.getRemote().sendString(gson.toJson(notificationMessage));
        }
    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ServerMessage error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        error.setError(errorMessage);
        session.getRemote().sendString(gson.toJson(error));
    }
}
