package dataaccess;

import service.AuthServiceTests;

public class AuthMemoryDAOTests extends AuthServiceTests {
    public AuthMemoryDAOTests() {
        super(createAuthMemoryDAO());
    }

    private static AuthMemoryDAO createAuthMemoryDAO() {
            return new AuthMemoryDAO();
    }
}