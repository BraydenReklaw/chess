package ui;

import java.util.Scanner;

public class UI {

    public static void main(String[] args) {
        PreLogIn();
    }

    public static void PreLogIn() {
        Scanner scanner = new Scanner(System.in);
        int selection = 0;
        while (selection != 2) {
            System.out.println("Welcome! Make a Selection");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Help");
            System.out.println("4. Quit");
            System.out.print("Make a selection: ");

            if (scanner.hasNext()) {
                selection = scanner.nextInt();
                switch (selection) {
                    case 4 -> System.out.println("Getting Help");
                    case 3 -> {
                        System.out.println("Exited");
                        break;
                    }
                    case 1 -> {
//                        userLogin();
                        PostLogIn(scanner, "Baymax");
                    }
                    case 2 -> {
//                        userRegister();
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
        String selection = null;
        while (selection != "2") {
            System.out.println(user + " logged in. Make a selection:");
            System.out.println("1. Help");
            System.out.println("2. Logout");
            System.out.println("3. Create a Game");
            System.out.println("4. List Games");
            System.out.println("5. Play Game");
            System.out.println("6. Observe Game");
            System.out.print("Make a selection: ");
        }
    }
}
