package server;

import chess.*;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class SocketHandler {

    private Map<Integer, GameSession> gameSessions = new HashMap<>();
    private Gson gson = new Gson();
    private GameSQLDAO gameDataAccess = new GameSQLDAO();
    private AuthSQLDAO authAccess = new AuthSQLDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException,
            InvalidMoveException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(session, command);
            case MAKE_MOVE -> handleMove(session, command);

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

    private void handleMove(Session session, UserGameCommand command) throws
            DataAccessException, IOException, InvalidMoveException {
        GameSession gameSession = gameSessions.get(command.getGameID());
        GameData gameData = gameDataAccess.getGame(command.getGameID());
        if (gameData == null) {
            sendError(session, "Game not found");
            return;
        }
        AuthData user = authAccess.getAuth(command.getAuthToken());
        if (user == null) {
            sendError(session, "Unauthorized");
            return;
        }
        ChessGame game = gameData.game();
        ChessMove move = command.getMove();
        ChessPosition start = move.getStartPosition();
        Collection<ChessMove> moves = game.validMoves(start);
        if (!moves.contains(move)) {
            sendError(session, "Unable to make move");
            return;
        }
        game.makeMove(move);
        GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), game);
        gameDataAccess.updateGame(updatedGame);
        for (Session clientSession : gameSession.getClients()) {
            sendLoadGame(clientSession, updatedGame);
        }
        sendNotificationOthers(gameSession, session, user.username() + " has made the move " +
                move.getStartPosition() + move.getEndPosition());
        String player;
        if (game.getTeamTurn() == ChessGame.TeamColor.WHITE) {
            player = gameData.whiteUsername();
        } else {
            player = gameData.blackUsername();
        }
        if (game.isInCheck(game.getTeamTurn())) {
            sendNotificationAll(gameSession, player + " is in Check");
        } else if (game.isInCheckmate(game.getTeamTurn())) {
            sendNotificationAll(gameSession, player + " is in Checkmate");
        } else if (game.isInStalemate(game.getTeamTurn())) {
            sendNotificationAll(gameSession, player + " is in Stalemate");
        }
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
