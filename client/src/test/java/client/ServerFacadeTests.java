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
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+ String.valueOf(port));
    }

    @AfterAll
    static void stopServer() throws Exception {
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
    public void testRegisterNeg() throws Exception {
        try {
            facade.register(new RegisterRequest("username", null, "email"));
        } catch (ResponseException ex) {
            System.out.println(ex.toString());
            assertEquals(400, ex.getStatusCode(), "Expeced 400 error for bad request");
            assertEquals("Error: bad request", ex.getMessage(), "Expeced error message 'Error: bad request'");
        }
    }

    @Test
    public void testLoginPos() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var authData = facade.login(new LoginRequest("username", "password"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void testLoginNeg() throws Exception {
        var resException = assertThrows(ResponseException.class, () -> {
            facade.login(new LoginRequest("user not registered", "password"));
        });

        ResponseException ex = (ResponseException) resException;
        assertEquals(401, ex.getStatusCode(), "Expected status 401 error");
        assertEquals("Error: unauthorized", ex.getMessage(), "Error message incorrect: " + ex.getMessage());
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
    public void testLogoutNeg() throws Exception {
        var exception = assertThrows(ResponseException.class, () -> {
            facade.logout("badAuthToken");
        });

        ResponseException resException = (ResponseException) exception;
        assertEquals(401, resException.getStatusCode(), "Expected 401: unauthorized");
    }

    @Test
    public void testCreateGamePos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var gameData = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        assertTrue(gameData.gameID() != null, "Expected gameID but recieved none");
    }
    
    @Test
    public void testCreateGameNeg() throws Exception {
        facade.register(new RegisterRequest("username", "password", "email"));
        var exception = assertThrows(ResponseException.class, () -> {
            facade.createGame(new CreateGameRequest("My Game"), "Bad Auth Token");
        });
        
        ResponseException resException = (ResponseException) exception;
        assertEquals(401, resException.getStatusCode(), "Expected 401 status");
    }
    
    @Test
    public void testListGamesPos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        ListGamesResponse gameList = facade.listGames(authData.authToken());
        var games = gameList.games();
        assertEquals(1, games.size(), "Expected only 1 game, found " + String.valueOf(games.size()));
    }

    @Test
    public void testListGamesNeg() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        var exception = assertThrows(ResponseException.class, () -> {
            facade.listGames("Bad auth token");
        });

        ResponseException resException = (ResponseException) exception;
        assertEquals(401, resException.getStatusCode(), "Expected 401 response code");
    }

    @Test
    public void testJoinGamePos() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var createResponse = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        facade.joinGame(new JoinGameRequest("WHITE", createResponse.gameID()), authData.authToken());
        var gameList = facade.listGames(authData.authToken()).games();

        var game = gameList.iterator().next();
        assertEquals(game.whiteUsername(), "username", "Username not added to game");
    }

    @Test
    public void testJoinGameNeg() throws Exception {
        var authData = facade.register(new RegisterRequest("username", "password", "email"));
        var createResponse = facade.createGame(new CreateGameRequest("My game"), authData.authToken());
        var exception = assertThrows(ResponseException.class, () -> {
            facade.joinGame(new JoinGameRequest("WHITE", createResponse.gameID()), "Bad auth token");
        });

        var resException = (ResponseException) exception;
        assertEquals(401, resException.getStatusCode(), "Expected 401 error");
    }

}
