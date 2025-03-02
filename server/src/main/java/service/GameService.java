package service;
import dataaccess.GameMemoryDAO;
import model.GameData;
import exception.ResponseException;
import java.util.Collection;

public class GameService {

    private final GameMemoryDAO gameDAO;

    public GameService(GameMemoryDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public GameData addGameData(String gameName) throws ResponseException {
        if ( gameDAO.get(gameName) == null ) { //need bad condition
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return gameDAO.add(gameName);
    }

    public Collection<GameData> listAllGameData() throws ResponseException {
        return gameDAO.listAll();
    }

    public GameData getGameData(String gameName) throws ResponseException {
        return gameDAO.get(gameName);
    }

    public void deleteGameData(String gameName) throws ResponseException {
        gameDAO.delete(gameName);
    }

    public void deleteAllGameData() throws ResponseException {
        gameDAO.deleteAll();
    }
}