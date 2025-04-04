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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            case RESIGN -> handleResign(session, command);
            case LEAVE -> handleLeave(session, command);
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
        ChessGame.TeamColor playerColor;
        if (Objects.equals(user.username(), gameData.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(user.username(), gameData.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            sendError(session, "You are observing, you cannot make moves");
            return;
        }
        ChessGame game = gameData.game();
        if (game.getGameOver()) {
            sendError(session, "This game has finished");
            return;
        }
        ChessMove move = command.getMove();
        if (game.getTeamTurn() != playerColor) {
            sendError(session, "Invalid Move, not your piece");
            return;
        }
        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            sendError(session, e.getMessage());
            return;
        }
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

    private void handleResign(Session session, UserGameCommand command) throws DataAccessException, IOException {
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
        if (!user.username().equals(gameData.whiteUsername()) && ! user.username().equals(gameData.blackUsername())) {
            sendError(session, "Observers can't resign");
            return;
        }
        if (gameData.game().getGameOver()) {
           sendError(session, "This game is already over");
           return;
        }
        gameData.game().setGameOver(true);
        GameData resignedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());
        gameDataAccess.updateGame(resignedGame);
        sendNotificationAll(gameSession, user.username() + " has resigned");
    }

    private void handleLeave(Session session, UserGameCommand command) throws DataAccessException, IOException {
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
        ChessGame.TeamColor playerColor;
        if (Objects.equals(user.username(), gameData.whiteUsername())) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(user.username(), gameData.blackUsername())) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            playerColor = null;
        }
        GameData updatedGame;
        if (playerColor != null) {
            if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                updatedGame = new GameData(gameData.gameID(), null, gameData.blackUsername(),
                        gameData.gameName(), gameData.game());
            } else {
                updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), null,
                        gameData.gameName(), gameData.game());
            }
            gameDataAccess.updateGame(updatedGame);
        }
        gameSession.removeClient(session, command.getAuthToken());
        sendNotificationOthers(gameSession, session, user.username() + " has left the game");
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
