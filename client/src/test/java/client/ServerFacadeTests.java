package client;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import exception.*;
import server.Server;
import server.ServerFacade;
import model.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+ port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clearApp(null);
    }

    @Test
    public void testRegisterPos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void testRegisterNeg() {
        try {
            facade.register(new RegisterRequest("username", null, "email"));
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
            assertEquals(400, ex.getStatusCode(), "Expected 400 error for bad request");
            assertEquals("Error: bad request", ex.getMessage(), "Expected error message 'Error: bad request'");
        }
    }

    @Test
    public void testLoginPos() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var authData = facade.login(new LoginRequest("username", "password"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void testLoginNeg() {
        var resException = assertThrows(ResponseException.class,
                () -> facade.login(new LoginRequest("user not registered", "password"))
        );

        assertEquals(401, resException.getStatusCode(), "Expected status 401 error");
        assertEquals("Error: unauthorized", resException.getMessage(), "Error message incorrect: " + resException.getMessage());
    }

    @Test
    public void testLogoutPos() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var authData = facade.login(new LoginRequest("username", "password"));
        if (authData.authToken().isEmpty()) {
            fail("empty authToken");
        }
        facade.logout(authData.authToken());
    }

    @Test
    public void testLogoutNeg() {
        var exception = assertThrows(ResponseException.class,
                () -> facade.logout("badAuthToken")
        );

        assertEquals(401, exception.getStatusCode(), "Expected 401: unauthorized");
    }

    @Test
    public void testCreateGamePos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var gameData = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        assertNotNull(gameData.gameID(), "Expected gameID but received none");
    }
    
    @Test
    public void testCreateGameNeg() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var exception = assertThrows(ResponseException.class, () ->
            facade.createGame(new CreateGameRequest("My Game"), "Bad Auth Token")
        );
        
        assertEquals(401, exception.getStatusCode(), "Expected 401 status");
    }
    
    @Test
    public void testListGamesPos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        ListGamesResponse gameList = facade.listGames(authData.authToken());
        var games = gameList.games();
        assertEquals(1, games.size(), "Expected only 1 game, found " + games.size());
    }

    @Test
    public void testListGamesNeg() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        var exception = assertThrows(ResponseException.class, () ->
            facade.listGames("Bad auth token")
        );

        assertEquals(401, exception.getStatusCode(), "Expected 401 response code");
    }

    @Test
    public void testJoinGamePos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var createResponse = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        facade.joinGame(new JoinGameRequest("WHITE", createResponse.gameID()), authData.authToken());
        var gameList = facade.listGames(authData.authToken()).games();

        var game = gameList.iterator().next();
        assertEquals("username", game.whiteUsername(), "Username not added to game");
    }

    @Test
    public void testJoinGameNeg() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var createResponse = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        var exception = assertThrows(ResponseException.class, () ->
            facade.joinGame(new JoinGameRequest("WHITE", createResponse.gameID()), "Bad auth token")
        );

        assertEquals(401, exception.getStatusCode(), "Expected 401 error");
    }

}
