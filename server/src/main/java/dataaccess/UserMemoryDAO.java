package dataaccess;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;
import java.util.HashMap;

public class UserMemoryDAO implements UserDAO {

    final private HashMap<String, UserData> userDb = new HashMap<>();

    public UserData add(UserData userData) throws DataAccessException {
        if (userDb.containsKey(userData.username())) {
            throw new DataAccessException("Error: already taken");
        }
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData finalUser = new UserData(userData.username(), hashedPassword, userData.email());
        userDb.put(finalUser.username(), finalUser);
        return finalUser;
    }

    public Collection<UserData> listAll() {
        return userDb.values();
    }


    public UserData get(String username) throws DataAccessException {
        if (this.userDb.containsKey(username)) {
            return userDb.get(username);
        } else {
            throw new DataAccessException("Error: " + username + " not found in userDb");
        }
    }

    public void delete(String username) {
        userDb.remove(username);
    }

    public void deleteAll() {
        userDb.clear();
    }
}