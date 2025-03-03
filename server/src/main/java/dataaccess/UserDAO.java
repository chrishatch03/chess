package dataaccess;
import model.UserData;
import java.util.Collection;

public interface UserDAO {
    UserData add(UserData userData) throws DataAccessException;
    Collection<UserData> listAll();
    UserData get(String username);
    void delete(String username);
    void deleteAll();
}
