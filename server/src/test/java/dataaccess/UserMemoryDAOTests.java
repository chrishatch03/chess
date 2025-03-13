package dataaccess;

import service.UserServiceTests;

public class UserMemoryDAOTests extends UserServiceTests {
    public UserMemoryDAOTests() {
        super(createUserMemoryDAO());
    }

    private static UserMemoryDAO createUserMemoryDAO() {
            return new UserMemoryDAO();
    }
}