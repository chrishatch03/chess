package service;
import dataaccess.AuthMemoryDAO;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    AuthService authService;

    @BeforeEach
    void init() {
        authService = new AuthService(new AuthMemoryDAO());
        authService.deleteAll();
    }

    @Test
    void testAddPos() {
        try {
            AuthData newAuthData = authService.add("username");
            assertNotNull(newAuthData, "New AuthData should not be null");
            assertEquals("username", newAuthData.username(), "Username should match");
        } catch (ResponseException ex) {
            fail("Failed to add session: " + ex.getMessage());
        }
    }

    @Test
    void testAddNeg() {
        try {
            authService.add(null);
            fail("Expected ResponseException for adding session with null username");
        } catch (ResponseException ex) {
            assertEquals(400, ex.getStatusCode(), "Should return 400 for null username");
        }
    }

    @Test
    void testGetPos() {
        try {
            AuthData newAuthData = authService.add("username");
            assertNotNull(newAuthData, "New AuthData should not be null");
            AuthData retrievedAuthData = authService.get(newAuthData.authToken());
            assertEquals(newAuthData, retrievedAuthData, "AuthData retrieved should match AuthData added");
        } catch (ResponseException ex) {
            fail("Failed to get session: " + ex.getMessage());
        }
    }

    @Test
    void testGetNeg() {
        try {
            AuthData newAuthData = authService.add("username");
            authService.get("invalid_token");
            fail("Expected ResponseException for invalid token");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for invalid session");
        }
    }

    @Test
    void testListAllPos() {
        try {
            authService.add("username");
            authService.add("secondUser");
            var authDataList = authService.listAll();
            assertEquals(2, authDataList.size(), "There should be 2 sessions listed");
        } catch (ResponseException ex) {
            fail("Failed to list sessions: " + ex.getMessage());
        }
    }

    @Test
    void testListAllNeg() {
        try {
            authService.add("username");
            authService.add("secondUser");
            var authDataList = authService.listAll();
            assertNotEquals(3, authDataList.size(), "Should not list 3 sessions");
        } catch (ResponseException ex) {
            fail("Failed to list sessions: " + ex.getMessage());
        }
    }

    @Test
    void testDeletePos() {
        try {
            AuthData session = authService.add("username");
            authService.add("secondUser");
            authService.delete(session.authToken());
            var authDataList = authService.listAll();
            assertEquals(1, authDataList.size(), "One session should remain after deletion");
        } catch (ResponseException ex) {
            fail("Failed to delete session: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteNeg() {
        try {
            authService.add("username");
            authService.delete("invalidUser");
            fail("Expected ResponseException for deleting non-existent session");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for trying to delete non-existent session");
        }
    }

    @Test
    void testDeleteAllPos() {
        try {
            authService.add("username");
            authService.add("secondUser");
            authService.deleteAll();
            var authDataList = authService.listAll();
            assertTrue(authDataList.isEmpty(), "All sessions should be deleted");
        } catch (ResponseException ex) {
            fail("Failed to delete all sessions: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteAllNeg() {
        try {
            authService.add("username");
            authService.add("secondUser");
            authService.deleteAll();
            var authDataList = authService.listAll();
            assertNotEquals(2, authDataList.size(), "Sessions should be deleted");
        } catch (ResponseException ex) {
            fail("Failed to delete all sessions: " + ex.getMessage());
        }
    }
}