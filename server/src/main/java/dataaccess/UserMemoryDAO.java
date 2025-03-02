package dataaccess;
import model.UserData;
import java.util.Collection;
import java.util.HashMap;

public class UserMemoryDAO {

    final private HashMap<String, UserData> userDb = new HashMap<>();

    public UserData add(UserData userData) {
        userData = new UserData(userData.username(), userData.password(), userData.email());
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