package dataaccess;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class AuthMemoryDAO {
    final private HashMap<String, AuthData> authDb = new HashMap<>();

    public AuthData add(AuthData authData) {
        authData = new AuthData(authData.authToken(), authData.username());

        authDb.put(authData.username(), authData);
        return authData;
    }

    public Collection<AuthData> listAll() {
        return authDb.values();
    }


    public AuthData get(String username) {
        return authDb.get(username);
    }

    public void delete(String username) {
        authDb.remove(username);
    }

    public void deleteAll() {
        authDb.clear();
    }
}
