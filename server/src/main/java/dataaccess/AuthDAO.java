package dataaccess;
import model.AuthData;
import java.util.Collection;

public interface AuthDAO {
    boolean sessionExists(String username);
    AuthData add(AuthData authData) throws DataAccessException;
    Collection<AuthData> listAll();
    AuthData get(String authToken) throws DataAccessException;
    void delete(String username);
    void deleteAll();
}
