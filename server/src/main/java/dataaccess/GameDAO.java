package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    GameData add(String gameName);
    GameData update(Integer gameId, GameData newGameData) throws DataAccessException;
    Collection<GameData> listAll();
    GameData get(Integer gameId) throws DataAccessException;
    void delete(Integer gameId);
    void deleteAll();
}