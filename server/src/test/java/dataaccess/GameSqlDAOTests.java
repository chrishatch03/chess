package dataaccess;

import service.GameServiceTests;

public class GameSqlDAOTests extends GameServiceTests {
    public GameSqlDAOTests() {
        super(createGameSqlDAO());
    }

    private static GameSqlDAO createGameSqlDAO() {
        try {
            return new GameSqlDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to initialize GameSqlDAO: " + e.getMessage(), e);
        }
    }
}