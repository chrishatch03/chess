package dataaccess;
import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class AuthMemoryDAO implements AuthDAO {
    final private HashMap<String, AuthData> authDb = new HashMap<>();

    public boolean sessionExists(String username) {
        for (AuthData authData: authDb.values()) {
            if (authData.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public AuthData add(AuthData authData) throws DataAccessException{
        if (authDb.containsKey(authData.authToken())) {
            throw new DataAccessException("Session already exists");
        }
        authDb.put(authData.authToken(), authData);
        return authData;
    }

    public Collection<AuthData> listAll() {
        return authDb.values();
    }


    public AuthData get(String authToken) throws DataAccessException {
//        AuthData authData = authDb.get(authToken);
//        if (this.sessionExists(authData.username())) {
//            return authData;
//        }
        if (this.authDb.containsKey(authToken)) {
            return authDb.get(authToken);
        }
        throw new DataAccessException("No session for " + authToken);
    }

    public void delete(String authToken) {
        authDb.remove(authToken);
    }

    public void deleteAll() {
        authDb.clear();
    }
}
