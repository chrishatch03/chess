package service;
import dataaccess.AuthDAO;
import dataaccess.AuthMemoryDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import exception.ResponseException;
import java.util.Collection;
import java.util.UUID;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthMemoryDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData add(String username) throws ResponseException {
        if (username == null) {
            throw new ResponseException(400, "Error: cannot get session for null username");
        }
        return authDAO.add(new AuthData(UUID.randomUUID().toString(), username));
    }

    public Collection<AuthData> listAll() {
        return authDAO.listAll();
    }

    public AuthData get(String authToken) throws ResponseException {
        try {
            return authDAO.get(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public AuthData sessionExists(String authToken) throws ResponseException {
        try {
            return authDAO.get(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public void delete(String authToken) throws ResponseException {
        try {
            authDAO.delete(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public void deleteAll() {
        authDAO.deleteAll();
    }
}