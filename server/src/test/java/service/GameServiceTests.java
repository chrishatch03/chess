package service;
import dataaccess.GameMemoryDAO;
import exception.ResponseException;
import model.GameData;
import model.JoinGameRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {

    GameService gameService;

    @BeforeEach
    void init() {
        try {
            gameService = new GameService(new GameMemoryDAO());
            gameService.deleteAll();
        } catch (Exception ex) {
            fail("Failed to initialize game service tests " + ex.getMessage());
        }
    }

    @Test
    void testAddPos() {
        try {
            GameData newGame = gameService.add("Chess Game");
            assertNotNull(newGame, "New GameData should not be null");
            assertEquals("Chess Game", newGame.gameName(), "Game name should match");
        } catch (ResponseException ex) {
            fail("Failed to add game: " + ex.getMessage());
        }
    }

    @Test
    void testAddNeg() {
        try {
            gameService.add("Chess Game");
            gameService.add("Chess Game");
            fail("Expected ResponseException for duplicate game name");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for duplicate game name");
        }
    }

    @Test
    void testJoinGamePos() {
        try {
            UserData user = new UserData("player1", "password", "player1@email.com");
            GameData chessGame = gameService.add("Chess Game");
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE",chessGame.gameID());
            GameData updatedGame = gameService.joinGame(joinGameRequest, user);
            assertEquals("player1", updatedGame.whiteUsername(), "White player's username should match");
        } catch (ResponseException ex) {
            fail("Failed to join game: " + ex.getMessage());
        }
    }

    @Test
    void testJoinGameNeg() {
        try {
            UserData user = new UserData("player1", "password", "player1@email.com");
            JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK",999);
            gameService.joinGame(joinGameRequest, user);
            fail("Expected ResponseException for trying to join a non-existent game");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for non-existent game");
        }
    }

    @Test
    void testListAllPos() {
        try {
            gameService.add("Chess Game");
            gameService.add("Tic Tac Toe");
            var gameList = gameService.listAll();
            assertEquals(2, gameList.size(), "There should be 2 games listed");
        } catch (ResponseException ex) {
            fail("Failed to list games: " + ex.getMessage());
        }
    }

    @Test
    void testListAllNeg() {
        try {
            gameService.add("Chess Game");
            gameService.add("Tic Tac Toe");
            var gameList = gameService.listAll();
            assertNotEquals(3, gameList.size(), "Should not list 3 games");
        } catch (ResponseException ex) {
            fail("Failed to list games: " + ex.getMessage());
        }
    }

    @Test
    void testGetPos() {
        try {
            GameData newGame = gameService.add("Chess Game");
            assertNotNull(newGame, "New GameData should not be null");
            GameData retrievedGame = gameService.get(newGame.gameID());
            assertEquals(newGame, retrievedGame, "GameData retrieved should match GameData added");
        } catch (ResponseException ex) {
            fail("Failed to get game: " + ex.getMessage());
        }
    }

    @Test
    void testGetNeg() {
        try {
            gameService.add("Chess Game");
            gameService.get(999);  // Non-existent game ID
            fail("Expected ResponseException for invalid game ID");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for invalid game ID");
        }
    }

    @Test
    void testDeletePos() {
        try {
            var game1 = gameService.add("Chess Game");
            var game2 = gameService.add("Tic Tac Toe");
            gameService.delete(game1.gameID());
            var gameList = gameService.listAll();
            assertEquals(1, gameList.size(), "There should be 1 game left");
        } catch (ResponseException ex) {
            fail("Failed to delete game: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteNeg() {
        try {
            var game1 = gameService.add("Chess Game");
            var game2 = gameService.add("Tic Tac Toe");
            gameService.delete(game1.gameID());
            gameService.delete(game1.gameID());
            fail("Expected ResponseException for trying to delete a non-existent game");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for non-existent game");
        }
    }

    @Test
    void testDeleteAllPos() {
        try {
            gameService.add("Chess Game");
            gameService.add("Tic Tac Toe");
            gameService.deleteAll();
            var gameList = gameService.listAll();
            assertTrue(gameList.isEmpty(), "All games should be deleted");
        } catch (ResponseException ex) {
            fail("Failed to delete all games: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteAllNeg() {
        try {
            gameService.add("Chess Game");
            gameService.add("Tic Tac Toe");
            gameService.deleteAll();
            var gameList = gameService.listAll();
            assertNotEquals(2, gameList.size(), "Games should be deleted");
        } catch (ResponseException ex) {
            fail("Failed to delete all games: " + ex.getMessage());
        }
    }
}