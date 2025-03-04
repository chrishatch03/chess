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
        int id = Math.abs(UUID.randomUUID().hashCode());
        GameData gameData = new GameData(id, null, null, gameName, null);
        gameDb.put(id, gameData);
        return gameData;
    }

    public GameData update(Integer gameId, GameData newGameData) throws DataAccessException {
        GameData oldGame = this.get(gameId);
        if (oldGame == null) {
            throw new DataAccessException("Cannot update game " + gameId.toString() + " because game does not exist");
        }
        gameDb.put(gameId, newGameData);
        return newGameData;
    }

    public Collection<GameData> listAll() {
        return gameDb.values();
    }


    public GameData get(Integer gameId) throws DataAccessException {
        GameData gameData = gameDb.get(gameId);
        if (gameData == null) {
            throw new DataAccessException("Game " + gameId + " does not exist in the database");
        }
        return gameData;
    }

    public void delete(Integer gameId) {
        gameDb.remove(gameId);
    }

    public void deleteAll() {
        gameDb.clear();
    }
}