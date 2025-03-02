package dataaccess;
import chess.ChessGame;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;

public class GameMemoryDAO {
    private int nextId = 1;
    final private HashMap<String, GameData> userDb = new HashMap<>();

    public GameData add(String gameName) {
        GameData gameData = new GameData(nextId++, null, null, gameName, null);

        userDb.put(gameData.gameName(), gameData);
        return gameData;
    }

    public Collection<GameData> listAll() {
        return userDb.values();
    }


    public GameData get(String username) {
        return userDb.get(username);
    }

    public void delete(String username) {
        userDb.remove(username);
    }

    public void deleteAll() {
        userDb.clear();
    }
}