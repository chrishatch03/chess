package service;
import exception.*;
import dataaccess.UserMemoryDAO;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    UserService userService;

    @BeforeEach
    void init() {
        userService = new UserService(new UserMemoryDAO());
        userService.deleteAll();
    }

    @Test
    void testRegisterPos() {
        try {
            UserData registeredUser = userService.register(new UserData("username", "password", "email@email.com"));
            assertNotNull(registeredUser, "Registered user should not be null");
            assertEquals("username", registeredUser.username(), "Username should match");
        } catch (ResponseException ex) {
            Assertions.fail("User registration failed: " + ex.getMessage());
        }
    }

    @Test
    void testRegisterNeg() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.register(new UserData("username", "password", "email@email.com")); // Duplicate username
            fail("Expected ResponseException for duplicate username");
        } catch (ResponseException ex) {
            assertEquals(403, ex.getStatusCode(), "Should return 403 for duplicate registration");
        }
    }

    @Test
    void testVerifyCredentialsBadUsername() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.verifyCredentials(new LoginRequest("bad username", "password"));
            fail("Expected ResponseException with status 401 due to bad username");
        } catch (ResponseException ex) {
            assertEquals(401, ex.getStatusCode(), "Should return 401 unauthorized due to bad username");
        }
    }

    @Test
    void testVerifyCredentialsBadPassword() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.verifyCredentials(new LoginRequest("username", "bad password"));
            fail("Expected ResponseException with status 401 due to bad password");
        } catch (ResponseException ex) {
            assertEquals(401, ex.getStatusCode(), "Should return 401 unauthorized due to bad password");
        }
    }

    @Test
    void testClear() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));

        } catch (ResponseException ex) {
            Assertions.fail("User registration failed: " + ex.getMessage());
        }
        userService.deleteAll();
        assertTrue(userService.listAll().isEmpty(), "Users Db was not cleared");
    }
}
