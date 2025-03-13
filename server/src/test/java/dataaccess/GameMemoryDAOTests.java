package dataaccess;

import service.GameServiceTests;

public class GameMemoryDAOTests extends GameServiceTests {
    public GameMemoryDAOTests() {
        super(createGameMemoryDAO());
    }

    private static GameMemoryDAO createGameMemoryDAO() {
            return new GameMemoryDAO();
    }
}