package dataaccess;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    GameData add(String gameName) throws DataAccessException;
    GameData update(Integer gameId, GameData newGameData) throws DataAccessException;
    Collection<GameData> listAll() throws DataAccessException;
    GameData get(Integer gameId) throws DataAccessException;
    void delete(Integer gameId) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}