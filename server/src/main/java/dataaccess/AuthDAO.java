package dataaccess;
import model.AuthData;
import java.util.Collection;

public interface AuthDAO {
    boolean sessionExists(String username) throws DataAccessException;
    AuthData add(AuthData authData) throws DataAccessException;
    Collection<AuthData> listAll() throws DataAccessException;
    AuthData get(String authToken) throws DataAccessException;
    void delete(String username) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}
