package dataaccess;
import model.UserData;
import java.util.Collection;

public interface UserDAO {
    UserData add(UserData userData) throws DataAccessException;
    Collection<UserData> listAll() throws DataAccessException;
    UserData get(String username) throws DataAccessException;
    void delete(String username) throws DataAccessException;
    void deleteAll() throws DataAccessException;
}
