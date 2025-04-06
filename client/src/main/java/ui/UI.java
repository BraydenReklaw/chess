package ui;

import model.AuthData;
import model.GameData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class UI {

    public static GameUI gameUI = new GameUI();

    public static void preLogIn() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        while (selection != 4) {
            System.out.println("Welcome! Make a Selection");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Help");
            System.out.println("4. Quit");
            System.out.print("Make a selection (number): ");

            if (scanner.hasNext()) {
                selection = preLogSelectionHandler(scanner);
            }
        }
        scanner.close();
    }

    public static int preLogSelectionHandler(Scanner scanner) throws IOException {
        int selection = scanner.nextInt();
        switch (selection) {
            case 3 -> System.out.println("To log in, select 1. To create a profile, select 2. " +
                    "To exit, select 4");
            case 4 -> {
                System.out.println("Exited");
            }
            case 1 -> {
                AuthData user = userLogin(scanner);
                if (user != null){
                    postLogIn(scanner, user);
                }
                else {
                    System.out.println("Oops, something went wrong. Username/password may not exist or " +
                            "an error occurred. Please Try Again");
                }
            }
            case 2 -> {
                AuthData user = userRegister(scanner);
                if (user != null) {
                    postLogIn(scanner, user);
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
        return selection;
    }
    public static void postLogIn(Scanner scanner, AuthData user) throws IOException {
        Collection<GameData> games = new ArrayList<>();
        int selection;
        while (true) {
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
                                "first select List Games with 2, then give a game number to Play Game or Observe " +
                                "Game to join or watch that game. Select 6 to Logout.");
                    }
                    case 4 -> {
                        observeGame(scanner, games, user);
                    }
                    case 3 -> {
                        playGame(scanner, user.authToken(), games, user);
                    }
                    case 2 -> {
                        games = listGames(user.authToken());
                    }
                    case 1 -> {
                        createGame(scanner, user.authToken());
                    }
                    default -> System.out.println("Invalid Choice, select a number.");
                }
            }
            System.out.println();
        }
    }

    public static AuthData userRegister(Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        System.out.print("Email: ");
        String email = scanner.next();
        return ServerFacade.register(username, password, email);
    }

    public static AuthData userLogin(Scanner scanner) throws IOException {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        return ServerFacade.logIn(username, password);
    }

    public static void userLogout(AuthData authData) throws IOException {
        ServerFacade.logOut(authData.authToken());
    }

    public static Collection<GameData> listGames(String token) throws IOException {
        Collection<GameData> games = ServerFacade.listGames(token);
        if (games == null) {
            System.out.println("An error has occurred.");
            return null;
        }
        else if (games.isEmpty()) {
            System.out.println("There are currently 0 games running.");
            return games;
        }
        System.out.printf("There are currently %d game(s) running", games.size());
        System.out.println();

        int index = 1;
        for (GameData game : games) {
            System.out.printf("%d. Name: %s, White Player: %s, Black Player: %s",
                    index, game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "OPEN",
                    game.blackUsername() != null ? game.blackUsername() : "OPEN");
            index++;
            System.out.println();
        }
        return games;
    }

    public static void createGame(Scanner scanner, String token) throws IOException {
        System.out.print("Name of Game: ");
        String gameName = scanner.next();
        String response = ServerFacade.createGame(token, gameName);
        if (response != null) {
            System.out.println("An error has occurred, please try again.");
        }
    }

    public static void observeGame(Scanner scanner, Collection<GameData> games, AuthData user) {
        String token = user.authToken();
        if (games.isEmpty() || games == null) {
            System.out.println("There are no games to Observe right now. Try List Games again.");
            return;
        }
        System.out.print("Select a game number: ");
        String selection = scanner.next();
        try{
            int index = Integer.parseInt(selection);
            if (index > games.size()) {
                System.out.println("This is not a valid game. Select another");
                return;
            }
            List<GameData> gameList = new ArrayList<>(games);

            GameData observeGame = gameList.get(index - 1);

            gameUI.gameplay(scanner, observeGame, "WHITE", user);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        }
    }

    public static void playGame(Scanner scanner, String token, Collection<GameData> games, AuthData user) {
        if (games.isEmpty() || games == null) {
            System.out.println("There are no games to Play right now. Try List Games again.");
            return;
        }
        System.out.print("Select a game number: ");
        String selection = scanner.next();
        try{
            int index = Integer.parseInt(selection);
            if (index > games.size() || index < 1) {
                System.out.println("This is not a valid game. Select another");
                return;
            }
            List<GameData> gameList = new ArrayList<>(games);

            GameData game = gameList.get(index - 1);

            System.out.printf("Current Players: White: %s, Black %s",
                    game.whiteUsername() != null ? game.whiteUsername() : "OPEN",
                    game.blackUsername() != null ? game.blackUsername() : "OPEN");
            System.out.println();
            System.out.print("Select a Color to play as (WHITE or BLACK): ");
            String player = scanner.next();
            if (player.equals("WHITE") || player.equals("BLACK")) {
                String response = ServerFacade.joinGame(token, player, game.gameID());
                if (response != null) {
                    System.out.println("An error occurred. Double check that you have chosen the right Game " +
                            "and color to play.");
                } else {
                    if (player.equals("WHITE")) {
                        game = new GameData(game.gameID(), user.username(), game.blackUsername(),
                                game.gameName(), game.game());
                    } else {
                        game = new GameData(game.gameID(), game.whiteUsername(), user.username(),
                                game.gameName(), game.game());
                    }
                    gameUI.gameplay(scanner, game, player, user);
                }
            } else {
                System.out.println("Invalid input. Please enter a valid color (WHITE / BLACK).");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
