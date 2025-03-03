package dataaccess;
import model.AuthData;
import model.UserData;
import java.util.Collection;
import java.util.HashMap;

public class UserMemoryDAO {

    final private HashMap<String, UserData> userDb = new HashMap<>();

    public UserData add(UserData userData) throws DataAccessException {
        if (userDb.containsKey(userData.username())) {
            throw new DataAccessException("Error: already taken");
        }
        userDb.put(userData.username(), userData);
        return userData;
    }

    public Collection<UserData> listAll() {
        return userDb.values();
    }


    public UserData get(String username) {
        return userDb.get(username);
    }

    public void delete(String username) {
        userDb.remove(username);
    }

    public void deleteAll() {
        userDb.clear();
    }
}