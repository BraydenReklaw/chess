package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Communicator;
import ui.ServerFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ServerFacadeTests {

    private static UserData testUser;
    private static Server server;

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(8810);
        System.out.println("Started test HTTP server on " + port);
        Communicator.testDelete("/db");
        testUser = new UserData("user1", "password", "email");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Communicator.testDelete("/db");
    }

    @Test
    public void successfulRegister() throws IOException {
        AuthData response = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotNull(response);
    }

    @Test
    public void registerExistingUser() throws IOException {
        ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNull(ServerFacade.register(testUser.username(),
                testUser.password(), testUser.email()));
    }

    @Test
    public void successfulLogin() throws IOException {
        ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        AuthData response = ServerFacade.logIn(testUser.username(), testUser.password());
        Assertions.assertNotNull(response);
    }

    @Test
    public void loginNonExistingUser() throws IOException {
        Assertions.assertNull(ServerFacade.logIn(testUser.username(), testUser.password()));
    }

    @Test
    public void successfulLogout() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotNull(token);
        ServerFacade.logOut(token.authToken());
        Assertions.assertNotNull(ServerFacade.logIn(testUser.username(), testUser.password()));
    }

    @Test
    public void logoutWithBadToken() throws IOException {
        ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotEquals("{}", Communicator.delete("/session", "abcd"));
    }

    @Test
    public void successfulCreateGame() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNull(ServerFacade.createGame(token.authToken(), "game1"));
    }

    @Test
    public void createGameBadName() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotNull(ServerFacade.createGame(token.authToken(), ""));
    }

    @Test
    public void successfulListGames() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        ServerFacade.createGame(token.authToken(), "game1");
        ServerFacade.createGame(token.authToken(), "game2");
        ServerFacade.createGame(token.authToken(), "game3");
        Assertions.assertEquals(3, ServerFacade.listGames(token.authToken()).size());
    }

    @Test
    public void listNoGames() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertEquals(0, ServerFacade.listGames(token.authToken()).size());
    }

    @Test
    public void successfulJoin()  throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        ServerFacade.createGame(token.authToken(), "game1");
        Collection<GameData> games = ServerFacade.listGames(token.authToken());
        List<GameData> gameList = new ArrayList<>(games);
        GameData game = gameList.getFirst();
        Assertions.assertNull(ServerFacade.joinGame(token.authToken(), "WHITE", game.gameID()));
    }

    @Test
    public void noGameToJoin() throws IOException {
        AuthData token = ServerFacade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotNull(ServerFacade.joinGame(token.authToken(), "WHITE", 1234));
    }
}
