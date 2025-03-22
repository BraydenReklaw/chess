package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Communicator;
import ui.ServerFacade;
import java.io.IOException;


public class ServerFacadeTests {

    private static UserData testUser;
    private static Server server;

    @BeforeAll
    public static void init(){
        server = new Server();
        var port = server.run(8810);
        System.out.println("Started test HTTP server on " + port);
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
}
