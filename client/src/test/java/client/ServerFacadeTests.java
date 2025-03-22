package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.Communicator;
import ui.ServerFacade;
import ui.UI;

import java.io.IOException;


public class ServerFacadeTests {

    private static UserData testUser;
    private static ServerFacade facade;
    private static Communicator communicator;

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8810);
        System.out.println("Started test HTTP server on " + port);

        testUser = new UserData("user1", "password", "email");

    }

    @AfterAll
    static void stopServer() throws IOException {
        communicator.delete("/db");
        server.stop();
    }

    @Test
    public void sampleTest(){
        Assertions.assertTrue(true);
    }

    @Test
    public void successfulRegister() throws IOException {
        String response = facade.register(testUser.username(), null, testUser.email());
        Assertions.assertEquals(response, "Registered");
    }

    @Test
    public void registerExistingUser() throws IOException {
        facade.register(testUser.username(), testUser.password(), testUser.email());
        Assertions.assertNotEquals("Registered", facade.register(testUser.username(),
                testUser.password(), testUser.email()));
    }
}
