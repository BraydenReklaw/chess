package ui;

import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Scanner;

public class GameUI {

    public void gameplay(Scanner scanner, GameData game, String playerType, AuthData user) {
//        System.out.println("transitioned to gameplay");
        GameData activeGame = game;
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
        DrawBoard.drawBoard(playerType, activeGame.game().getBoard());
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
                        DrawBoard.drawBoard(playerType, activeGame.game().getBoard());
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
                    }
                }
            }
        }

    }
}
