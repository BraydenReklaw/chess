package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public class GameUI implements ServerMessageObserver {

    public static DrawBoard drawer;

    public GameUI (ChessGame game) {
        drawer = new DrawBoard(game);
    }

    public void gameplay(Scanner scanner, int gameID, String playerColor, AuthData user) {
        connect(gameID, user);
        ui(scanner, gameID, playerColor, user);
    }

    public void connect(int gameID, AuthData user) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    user.authToken(), gameID, null);
            ServerFacade.sendCommand(command);
        } catch (IOException e) {
            System.out.println("An error occurred connecting to the game");
            return;
        }
    }

    public void ui(Scanner scanner, int gameID, String playerColor, AuthData user) {
        drawer.drawBoard(playerColor, null);
        int selection = 0;
        while (selection != 5) {
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
                    case 6 -> loadHelp();
                    case 1 -> drawer.drawBoard(playerColor, null);
                    case 5 -> {
                        handleLeave(gameID, user);
                    }
                    case 4 -> handleResign(scanner, gameID, user);
                    case 2 -> handleHighlight(scanner, playerColor);
                    case 3 -> handleMakeMove(scanner, gameID, user);
                    default -> System.out.println("Invalid selection, please select 1-6 only");
                }
                System.out.println();
            }
        }
    }

    public void loadHelp() {
        System.out.println("To redraw the current board, select 1.");
        System.out.println("To see any piece's legal moves select 2.");
        System.out.println("To make a move, select 3 and give the coordinates as prompted. " +
                "(Only Available to players)");
        System.out.println("To resign and forfeit the game, select 4. (Only available to players)");
        System.out.println("To leave the game, select 5.");
    }

    public void handleLeave(int gameID, AuthData user) {
        try {
            UserGameCommand leave =  new UserGameCommand(
                    UserGameCommand.CommandType.LEAVE, user.authToken(), gameID, null);
            ServerFacade.sendCommand(leave);
        } catch (IOException e) {
            System.out.println("An error occurred while leaving the game");
        }
    }

    public void handleResign(Scanner scanner, int gameID, AuthData user) {
        System.out.println("Are you sure you would like to resign? (This is irreversible) (y/n): ");
        String resignChoice = scanner.next();
        if (resignChoice.equals("y")) {
            try {
                UserGameCommand resign = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                        user.authToken(), gameID, null);
                ServerFacade.sendCommand(resign);
            } catch (IOException e) {
                System.out.println("An error occurred will resigning the Game");
            }
        } else if (!resignChoice.equals("n")) {
            System.out.println("Invalid selection, please try again.");
        } else {
            System.out.println("Returning to gameplay");
        }
    }

    public void handleHighlight(Scanner scanner, String playerColor) {
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
                drawer.drawBoard(playerColor, position);
            } else {
                System.out.println("Invalid selection, please try again.");
            }
        }
    }

    public void handleMakeMove(Scanner scanner, int gameID, AuthData user) {
        System.out.println("Please input starting coordinate (ex:a2): ");
        String coordinate;
        String endCoordinate;
        while (true) {
            coordinate = scanner.next();
            if (coordinate.length() != 2) {
                System.out.println("Invalid Selection, please try again.");
            } else if (!coordinate.matches("^[a-h][1-8]$")) {
                System.out.println("Invalid selection, please try again.");
            } else {
                break;
            }
        }
        System.out.println("Please input starting coordinate (ex:a3): ");
        while (true) {
            endCoordinate = scanner.next();
            if (endCoordinate.length() != 2) {
                System.out.println("Invalid Selection, please try again.");
            } else if (!endCoordinate.matches("^[a-h][1-8]$")) {
                System.out.println("Invalid selection, please try again.");
            } else {
                break;
            }
        }
        char colChar = coordinate.charAt(0);
        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(coordinate.charAt(1));
        ChessPosition startPosition = new ChessPosition(row, col);
        colChar = endCoordinate.charAt(0);
        col = colChar - 'a' + 1;
        row = Character.getNumericValue(endCoordinate.charAt(1));
        ChessPosition endPosition = new ChessPosition(row, col);
        ChessMove userMove = null;
        boolean promote = false;
        ChessGame activeGame = drawer.getGame();
        Collection<ChessMove> moves = activeGame.validMoves(startPosition);
        for (ChessMove move: moves) {
            if (move.getPromotionPiece() != null) {
                promote = true;
                break;
            }
        }
        if (promote) {
            System.out.println("Please select a promotion piece ((r)ook, (b)ishop, (k)night, (q)ueen): ");
            while(true) {
                String promotion = scanner.next();
                if (promotion.equals("r") || promotion.equals("b") || promotion.equals("k") || promotion.equals("q")) {
                    switch(promotion) {
                        case "r" -> userMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK);
                        case "b" -> userMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP);
                        case "k" -> userMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT);
                        case "q" -> userMove = new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
                    }
                    break;
                } else {
                    System.out.println("Invalid Selection, please try again");
                }
            }
        } else {
            userMove = new ChessMove(startPosition, endPosition, null);
        }
        UserGameCommand makeMove = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE,
                user.authToken(), gameID, userMove);
        try {
            ServerFacade.sendCommand(makeMove);
        } catch (IOException e) {
            System.out.println("An Error Occurred while making a move");
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> notification(message);
            case ERROR -> error(message);
            case LOAD_GAME -> {
                drawer.updateGame(message.getGame().game());
                System.out.println("The Chessboard has been updated. It is recommended to (re)draw the Board.");
            }
        }
    }

    public void notification(ServerMessage serverMessage) {
        String message = serverMessage.getMessage();
        System.out.println("***** " + message + " *****");
    }

    public void error(ServerMessage errorMessage) {
        String error = errorMessage.getErrorMessage();
        System.out.println("***** Error: " + error + " *****");
    }
}
