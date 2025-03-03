package dataaccess;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class AuthMemoryDAO {
    final private HashMap<String, AuthData> authDb = new HashMap<>();

    public boolean sessionExists(String username) {
        for (AuthData authData: authDb.values()) {
            if (authData.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public AuthData add(AuthData authData) {
        authDb.put(authData.authToken(), authData);
        return authData;
    }

    public Collection<AuthData> listAll() {
        return authDb.values();
    }


    public AuthData get(String authToken) throws DataAccessException {
        AuthData authData = authDb.get(authToken);
        if (this.sessionExists(authData.username())) {
            return authData;
        }
        throw new DataAccessException("No session for " + authToken);
    }

    public void delete(String username) {
        authDb.remove(username);
    }

    public void deleteAll() {
        authDb.clear();
    }
}
