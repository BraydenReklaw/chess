package ui;

import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Scanner;

public class GameUI implements ServerMessageObserver {

    private GameData activeGame;

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> notification(message);
            case ERROR -> error(message);
            case LOAD_GAME -> loadGame(message);
        }
    }

    public void gameplay(Scanner scanner, GameData game, String playerType, AuthData user) {
//        System.out.println("transitioned to gameplay");
        activeGame = game;
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    user.authToken(), game.gameID(), null);
            ServerFacade.sendCommand(command);
        } catch (IOException e) {
            System.out.println("An error occurred connecting to the game");
            return;
        }
        int selection;
        System.out.println("Welcome to " + game.gameName() + ", " + user.username() + "!");
        DrawBoard.drawBoard(playerType, activeGame.game(), null);
        while (true) {
            System.out.println("Please make a selection:");
            System.out.println("1. Draw Board");
            System.out.println("2. Highlight Legal Moves");
            System.out.println("3. Make Move");
            System.out.println("4. Resign");
            System.out.println("5. Leave");
            System.out.println("6. Help");
            System.out.print("Make a selection (number): ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 6 -> {
                        System.out.println("To redraw the current board, select 1. To see any piece's legal moves"
                        + " select 2. To make a move, select 3 and give the coordinates as prompted. To resign "
                        + "the game, select 4. To Leave, select 5. Observers may not select 3 or 4.");
                    }
                    case 1 -> {
                        DrawBoard.drawBoard(playerType, activeGame.game(), null);
                    }
                    case 5 -> {
                        try {
                            UserGameCommand leave =  new UserGameCommand(
                                    UserGameCommand.CommandType.LEAVE,
                                    user.authToken(),
                                    activeGame.gameID(),
                                    null);
                            ServerFacade.sendCommand(leave);
                        } catch (IOException e) {
                            System.out.println("An error occurred leaving the game");
                        }
                        return;
                    }
                    case 4 -> {
                        System.out.println("Are you sure you would like to resign? (This is irreversible) (y/n): ");
                        String resignChoice = scanner.next();
                        if (resignChoice.equals("y")) {
                            try {
                                UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                                        user.authToken(), activeGame.gameID(), null);
                                ServerFacade.sendCommand(resign);
                            } catch (IOException e) {
                                System.out.println("An error occurred will resigning the Game");
                            }
                        } else if (resignChoice.equals("n")) {
                            continue;
                        } else {
                            System.out.println("Invalid selection, please try again.");
                        }
                    }
                    case 2 -> {
                        System.out.println("Please input a coordinate (ex: a1): ");
                        String coordinate = scanner.next();
                        if (coordinate.length() != 2) {
                            System.out.println("Invalid selection, please try again.");
                        } else {
                            if (coordinate.matches("^[a-h][1-8]$")) {
                                char colChar = coordinate.charAt(0);
                                int col = colChar - 'a' + 1;
                                int row = Character.getNumericValue(coordinate.charAt(1));
                                ChessPosition position = new ChessPosition(row, col);
                                DrawBoard.drawBoard(playerType, activeGame.game(), position);
                            } else {
                                System.out.println("Invalid selection, please try again.");
                            }
                        }
                    }
                }
            }
        }

    }

    public void notification(ServerMessage serverMessage) {
        String message = serverMessage.getMessage();
        System.out.println(message);
    }

    public void error(ServerMessage errorMessage) {
        String error = errorMessage.getErrorMessage();
        System.out.println("Error: " + error);
    }

    public void loadGame(ServerMessage gameMessage) {
        activeGame = gameMessage.getGame();
        System.out.println("The Chessboard has been updated. Please redraw it.");
    }
}
