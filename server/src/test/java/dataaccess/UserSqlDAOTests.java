package dataaccess;

import service.UserServiceTests;

public class UserSqlDAOTests extends UserServiceTests {
    public UserSqlDAOTests() {
        super(createUserSqlDAO());
    }

    private static UserSqlDAO createUserSqlDAO() {
        try {
            return new UserSqlDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize UserSqlDAO: " + e.getMessage(), e);
        }
    }
}