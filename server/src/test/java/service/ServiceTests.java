package service;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import dataaccess.*;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.UUID;

public class ServiceTests {
    private UserDataAccess userData;
    private AuthDataAccess authData;
    private GameDataAccess gameData;
    private ClearService clearService;
    private UserService userService;
    private GameService gameService;
    private UserData testUser1;
    private AuthData testAuth1;
    private GameData testGame1;


    @BeforeEach
    public void setup() {
        userData = new UserDataAccess();
        authData = new AuthDataAccess();
        gameData = new GameDataAccess();
        clearService = new ClearService(userData, authData, gameData);
        userService = new UserService(userData, authData);
        gameService = new GameService(gameData, authData);
        testUser1 = new UserData("user1", "password1", "email");
        testAuth1 = new AuthData(UUID.randomUUID().toString(), "user1");
    }

    @Test
    public void successfulClear() {
        userData.createUser(testUser1);
        authData.createAuth(testAuth1);
        gameData.createGame("testGameClear");

        clearService.clear();

        Assertions.assertTrue(userData.isEmpty());
        Assertions.assertTrue(authData.isEmpty());
        Assertions.assertTrue(gameData.isEmpty());
    }



    @Test
    public void successfulRegister() throws DataAccessException {
        AuthData actual = userService.register(testUser1);

        Assertions.assertFalse(userData.isEmpty());
        Assertions.assertFalse(authData.isEmpty());
        Assertions.assertSame(authData.getAuth(actual.authToken()), actual);
    }

    @Test
    public void userAlreadyPresentRegister() throws DataAccessException {
        userService.register(testUser1);

        Assertions.assertThrows(DataAccessException.class, () -> userService.register(testUser1));
    }

    @Test
    public void successfulLogin() throws DataAccessException {
        AuthData registerAuth = userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);

        Assertions.assertNotSame(registerAuth.authToken(), testAuth.authToken());
    }

    @Test
    public void unregisteredUserLogin() {
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(testUser1));
    }

    @Test
    public void badPasswordLogin() throws DataAccessException {
        userService.register(testUser1);
        UserData testUser2 = new UserData("user1", "password2", "email");

        Assertions.assertThrows(DataAccessException.class, () -> userService.login(testUser2));
    }

    @Test
    public void successfulLogout() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        userService.logout(testAuth.authToken());

        Assertions.assertNull(authData.getAuth(testAuth.authToken()));
    }

    @Test
    public void logoutWithoutRegister() {
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(testAuth1.authToken()));
    }

    @Test
    public void logoutWithoutLogin() throws DataAccessException {
        userService.register(testUser1);
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(testAuth1.authToken()));
    }

    @Test
    public void successfulList() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        Collection<GameData> gameList = gameService.list(testAuth.authToken());

        Assertions.assertEquals(0, gameList.size());

        gameData.createGame("testGame1");
        gameData.createGame("testGame2");
        gameData.createGame("testGame3");

        gameList = gameService.list(testAuth.authToken());

        Assertions.assertEquals(3, gameList.size());
    }

    @Test
    public void unauthorizedList() {
        Assertions.assertThrows(DataAccessException.class, () -> gameService.list(testAuth1.authToken()));
    }

    @Test
    public void successfulCreateGame() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        String gameID = gameService.create(testAuth.authToken(), "testGame1");
        GameData createdGame = gameData.getGame(Integer.parseInt(gameID));
        Collection <GameData> gameList = gameService.list(testAuth.authToken());

        Assertions.assertEquals(1, gameList.size());
        Assertions.assertSame("testGame1", createdGame.gameName());
    }

    @Test
    public void unauthorizedCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.create(testAuth1.authToken(), "testGame1"));
    }

    @Test
    public void successfulJoinGame() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        GameData testGame = gameData.createGame("testGame");
        gameService.join(testAuth.authToken(), testGame, "WHITE");
        GameData updatedGame = gameData.getGame(testGame.gameID());
        Collection <GameData> gameList = gameService.list(testAuth.authToken());

        Assertions.assertNotNull(updatedGame.whiteUsername());
        Assertions.assertSame(testAuth.username(), updatedGame.whiteUsername());
        Assertions.assertSame(1, gameList.size());
    }

    @Test
    public void unauthorizedJoin() {
        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.join(testAuth1.authToken(), gameData.createGame("testGame"), "WHITE"));
    }

    @Test
    public void noGameToJoin() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        GameData testGame = new GameData(1234, null, null, "testGame", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.join(testAuth.authToken(), testGame, "WHITE"));
    }

    @Test
    public void playerAlreadyTaken() throws DataAccessException {
        userService.register(testUser1);
        AuthData testAuth = userService.login(testUser1);
        GameData testGame = gameData.createGame("testGame");
        gameService.join(testAuth.authToken(), testGame, "WHITE");

        Assertions.assertThrows(DataAccessException.class, () ->
                gameService.join(testAuth.authToken(), testGame, "WHITE"));
    }
}
