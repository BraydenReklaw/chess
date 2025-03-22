package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import java.io.IOException;
import java.util.Scanner;

public class UI {
    private static ChessGame defaultGame = new ChessGame();
    private static GameData defaultGameData = new GameData(1234, null, null,
            "game1", defaultGame);

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        PreLogIn(scanner);
        scanner.close();
    }

    public static void PreLogIn(Scanner scanner) throws IOException {
        int selection = 0;
        while (selection != 4) {
            System.out.println("Welcome! Make a Selection");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Help");
            System.out.println("4. Quit");
            System.out.print("Make a selection (number): ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 3 -> System.out.println("To log in, select 1. To create a profile, select 2. " +
                            "To exit, select 4");
                    case 4 -> {
                        System.out.println("Exited");
                    }
                    case 1 -> {
                        AuthData user = userLogin(scanner);
                        if (user != null){
                            PostLogIn(scanner, user);
                        } else {
                            System.out.println("Oops, something went wrong. Username may not exist or an" +
                                    " error occurred. Please Try Again");
                        }
                    }
                    case 2 -> {
                        AuthData user = userRegister(scanner);
                        if (user != null) {
                            PostLogIn(scanner, user);
                        } else {
                          System.out.println("Oops, something went wrong. Username may be taken or an " +
                                  "error occurred. Please try again");
                        }
                    }
                    default -> {
                        System.out.println("Invalid Choice. Select 1, 2, 3, or 4.");
                    }
                }
                System.out.println();
            }
        }
    }

    public static void PostLogIn(Scanner scanner, AuthData user) throws IOException {

        int selection = 0;
        while (selection != 6) {
            System.out.println(user.username() + " logged in. Make a selection:");
            System.out.println("1. Create a Game");
            System.out.println("2. List Games");
            System.out.println("3. Play Game");
            System.out.println("4. Observe Game");
            System.out.println("5. Help");
            System.out.println("6. Logout");
            System.out.print("Make a selection (number): ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 6 -> {
                        userLogout(user);
                        System.out.println("Logging Out");
                        return;
                    }
                    case 5 -> {
                        System.out.println("To create a Game, select 1 and enter a name. To Play or Observe a Game, " +
                                "first select List Games with 2, then give a Game number to Play Game or Observe " +
                                "Game to join or watch that game. Select 6 to Logout");
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

    public static AuthData userRegister(Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        AuthData response = ServerFacade.register(username, password, email);
        return response;
    }

    public static AuthData userLogin(Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        AuthData response = ServerFacade.logIn(username, password);
        return response;
    }

    public static void userLogout(AuthData authData) throws IOException {
        ServerFacade.logOut(authData.authToken());
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
