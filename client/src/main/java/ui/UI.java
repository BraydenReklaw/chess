package ui;

import chess.ChessGame;
import model.GameData;

import java.util.Scanner;

public class UI {
    private static ChessGame defaultGame = new ChessGame();
    private static GameData defaultGameData = new GameData(1234, null, null, "game1", defaultGame);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PreLogIn(scanner);
        scanner.close();
    }

    public static void PreLogIn(Scanner scanner) {
        int selection = 0;
        while (selection != 4) {
            System.out.println("Welcome! Make a Selection");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Help");
            System.out.println("4. Quit");
            System.out.print("Make a selection: ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 3 -> System.out.println("Getting Help");
                    case 4 -> {
                        System.out.println("Exited");
                        break;
                    }
                    case 1 -> {
//                        UserLogin();
                        PostLogIn(scanner, "Baymax");
                    }
                    case 2 -> {
//                        UserRegister();
                        PostLogIn(scanner, "Baymax");
                    }
                    default -> {
                        System.out.println("Invalid Choice. Select 1, 2, 3, or 4.");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void PostLogIn(Scanner scanner, String user) {

        int selection = 0;
        while (selection != 6) {
            System.out.println(user + " logged in. Make a selection:");
            System.out.println("1. Create a Game");
            System.out.println("2. List Games");
            System.out.println("3. Play Game");
            System.out.println("4. Observe Game");
            System.out.println("5. Help");
            System.out.println("6. Logout");
            System.out.print("Make a selection: ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 6 -> {
//                        UserLogout();
                        return;
                    }
                    case 5 -> {
                        System.out.println("Getting Help");
                    }
                    case 4 -> {
                        ObserveGame();
                    }
                    case 3 -> {
//                        PlayGame();
                    }
                    case 2 -> {
                        ListGames();
                    }
                    case 1 -> {
//                        CreateGame();
                    }
                    default -> System.out.println("Invalid Choice, select a number.");
                }
            }
        }
    }

    public static void ListGames() {
        System.out.println("There are currently 1 Game(s):");
        System.out.print("1. ");
        System.out.print(defaultGameData.gameName());
        System.out.print(", players: White: ");
        System.out.print(defaultGameData.whiteUsername());
        System.out.print(", Black: ");
        System.out.println(defaultGameData.blackUsername());
    }

    public static void ObserveGame() {
        DrawBoard.DrawBoard("WHITE", defaultGame.getBoard());
    }
}
