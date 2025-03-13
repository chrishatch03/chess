package dataaccess;

import service.AuthServiceTests;

public class AuthSqlDAOTests extends AuthServiceTests {
    public AuthSqlDAOTests() {
        super(createAuthSqlDAO());
    }

    private static AuthSqlDAO createAuthSqlDAO() {
        try {
            return new AuthSqlDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize AuthSqlDAO: " + e.getMessage(), e);
        }
    }
}