package service;
import exception.*;
import dataaccess.UserMemoryDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    UserService userService = new UserService(new UserMemoryDAO());

    @BeforeEach
    void init() {
        userService.deleteAll();
    }

    @Test
    void testClear() {
        try {
            userService.register(new UserData("username", "password", "email@email.com"));
        } catch (ResponseException ex) {
            Assertions.fail("User registration failed: " + ex.getMessage());
        }
        userService.deleteAll();
        assertTrue(userService.listAll().isEmpty(), "Game was not cleared");
    }
}
