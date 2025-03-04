package dataaccess;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class GameMemoryDAO implements GameDAO {
    final private HashMap<Integer, GameData> gameDb = new HashMap<>();


    public GameData add(String gameName) throws DataAccessException {
        for (GameData game : gameDb.values()) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Game name already taken");
            }
        }
        int gameID = Math.abs(UUID.randomUUID().hashCode());
        GameData gameData = new GameData(gameID, null, null, gameName, null);
        gameDb.put(gameID, gameData);
        return gameData;
    }

    public GameData update(Integer gameID, GameData newGameData) throws DataAccessException {
        if (!gameDb.containsKey(gameID)) {
            throw new DataAccessException("Cannot update game " + gameID.toString() + " because game does not exist");
        }
        gameDb.remove(gameID);
        gameDb.put(gameID, newGameData);
        return newGameData;
    }

    public Collection<GameData> listAll() {
        return gameDb.values();
    }


    public GameData get(Integer gameID) throws DataAccessException {
        if (!gameDb.containsKey(gameID)) {
            throw new DataAccessException("Game " + gameID.toString() + " does not exist");
        }
        return gameDb.get(gameID);
    }

    public void delete(Integer gameID) {
        gameDb.remove(gameID);
    }

    public void deleteAll() {
        gameDb.clear();
    }
}