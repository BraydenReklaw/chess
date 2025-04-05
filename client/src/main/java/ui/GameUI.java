package ui;

import model.AuthData;
import model.GameData;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Scanner;

public class GameUI {

    public void gameplay(Scanner scanner, GameData game, String playerType, AuthData user) {
        System.out.println("transitioned to gameplay");
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                    user.authToken(), game.gameID(), null);
            ServerFacade.sendCommand(command);
        } catch (IOException e) {

        }
    }
}
