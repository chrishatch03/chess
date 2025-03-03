package service;
import exception.*;
import dataaccess.UserMemoryDAO;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
            fail("User registration failed: " + ex.getMessage());
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
    void testVerifyCredentialsPos() {
        try {
            UserData registeredUser = userService.register(new UserData("username", "password", "email@email.com"));
            UserData userData = userService.verifyCredentials(new LoginRequest("username", "password"));
            assertNotNull(userData, "User verification should return a non-null user");
            assertEquals(registeredUser.username(), userData.username(), "usernames should match");
            assertEquals(registeredUser.password(), userData.password(), "passwords should match");
            assertEquals(registeredUser.email(), userData.email(), "emails should match");
        } catch (ResponseException ex) {
            fail("Login Failed: " + ex.getMessage());
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
    void testListAllPos() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.register(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            Collection<UserData> userList = userService.listAll();
            var expected = new HashSet<>();
            expected.add(new UserData("username", "password", "email@email.com"));
            expected.add(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));

            assertEquals(expected, new HashSet<>(userList), "User sets do not match");
        } catch (ResponseException ex) {
            fail("ListAll Failed: " + ex.getMessage());
        }
    }

    @Test
    void testListAllNeg() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.register(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            Collection<UserData> userList = userService.listAll();
            var expected = new HashSet<>();
            expected.add(new UserData("username", "password", "email@email.com"));
            expected.add(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            expected.add(new UserData("thirdUser", "thirdPassword", "thirdEmail@email.com"));

            assertNotEquals(expected, new HashSet<>(userList), "User sets should not match");
        } catch (ResponseException ex) {
            fail("ListAll Failed: " + ex.getMessage());
        }
    }

    @Test
    void testGetPos() {
        try {
            UserData registeredUser = userService.register(new UserData("username", "password", "email@email.com"));
            assertNotNull(registeredUser, "Registered user should not be null");
            UserData retrievedUser = userService.get("username");
            assertEquals(registeredUser, retrievedUser, "User retrieved should match user registered");
        } catch (ResponseException ex) {
            fail("Failed to get user: " + ex.getMessage());
        }
    }

    @Test
    void testGetNeg() {
        try {
            UserData registeredUser = userService.register(new UserData("username", "password", "email@email.com"));
            assertNotNull(registeredUser, "Registered user should not be null");
            userService.get("bad username");
            fail("Expected ResponseException for requesting bad username");
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode(), "Should return 500 for internal server error");
        }
    }

    @Test
    void testDeletePos() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.register(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            userService.register(new UserData("thirdUser", "thirdPassword", "thirdEmail@email.com"));
            userService.delete("thirdUser");
            Collection<UserData> userList = userService.listAll();
            var expected = new HashSet<>();
            expected.add(new UserData("username", "password", "email@email.com"));
            expected.add(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));

            assertEquals(expected, new HashSet<>(userList), "User sets do not match");
        } catch (ResponseException ex) {
            fail("Delete Failed when should have succeeded: " + ex.getMessage());
        }
    }

    @Test
    void testDeleteNeg() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
            userService.register(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            userService.register(new UserData("thirdUser", "thirdPassword", "thirdEmail@email.com"));
            userService.delete("thirdUser");
            Collection<UserData> userList = userService.listAll();
            var expected = new HashSet<>();
            expected.add(new UserData("username", "password", "email@email.com"));
            expected.add(new UserData("secondUser", "secondPassword", "secondEmail@email.com"));
            expected.add(new UserData("thirdUser", "thirdPassword", "thirdEmail@email.com"));

            assertNotEquals(expected, new HashSet<>(userList), "User sets do not match");
        } catch (ResponseException ex) {
            fail("Delete succeeded when should have failed: " + ex.getMessage());
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
