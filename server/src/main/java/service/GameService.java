package service;
import dataaccess.DataAccess;
import model.GameData;
import model.GameDataType;
import exception.ResponseException;
import java.util.Collection;

public class GameService {

    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameData addGameData(GameData gameData) throws ResponseException {
        if (gameData.type() == GameDataType.DOG && gameData.name().equals("fleas")) {
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return dataAccess.addGameData(gameData);
    }

    public Collection<GameData> listAllGameData() throws ResponseException {
        return dataAccess.listAllGameData();
    }

    public GameData getGameData(int id) throws ResponseException {
        return dataAccess.getGameData(id);
    }

    public void deleteGameData(Integer id) throws ResponseException {
        dataAccess.deleteGameData(id);
    }

    public void deleteAllGameData() throws ResponseException {
        dataAccess.deleteAllGameData();
    }
}