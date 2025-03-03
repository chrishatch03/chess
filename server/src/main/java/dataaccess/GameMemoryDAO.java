package dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;

public class GameMemoryDAO implements GameDAO {
    private int nextId = 1;
    final private HashMap<Integer, GameData> gameDb = new HashMap<>();

    public GameData add(String gameName) {
        GameData gameData = new GameData(nextId++, null, null, gameName, null);

        gameDb.put(gameData.gameId(), gameData);
        return gameData;
    }

    public GameData update(Integer gameId, GameData newGameData) throws DataAccessException {
        GameData oldGame = this.get(gameId);
        if (oldGame == null) {
            throw new DataAccessException("Error: Cannot update game " + gameId.toString() + " because game does not exist");
        }
        gameDb.put(gameId, newGameData);
        return newGameData;
    }

    public Collection<GameData> listAll() {
        return gameDb.values();
    }


    public GameData get(Integer gameId) throws DataAccessException {
        GameData gameData = this.get(gameId);
        if (gameData == null) {
            throw new DataAccessException("Error: Game " + gameId.toString() + " does not exist in the database");
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