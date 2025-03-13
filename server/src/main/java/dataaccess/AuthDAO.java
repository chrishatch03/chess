package dataaccess;
import model.AuthData;

import java.sql.SQLException;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public interface AuthDAO {
    boolean sessionExists(String username) throws DataAccessException;
    AuthData add(AuthData authData) throws DataAccessException;
    Collection<AuthData> listAll() throws DataAccessException;
    AuthData get(String authToken) throws DataAccessException;
    void delete(String username) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}
