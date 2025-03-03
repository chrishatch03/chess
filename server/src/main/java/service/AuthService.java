package service;
import dataaccess.AuthMemoryDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import exception.ResponseException;
import java.util.Collection;
import java.util.UUID;

public class AuthService {

    private final AuthMemoryDAO authDAO;

    public AuthService(AuthMemoryDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData add(String username) {
        return authDAO.add(new AuthData(UUID.randomUUID().toString(), username));
    }

    public Collection<AuthData> listAll() throws ResponseException {
        return authDAO.listAll();
    }

    public AuthData get(String authToken) throws ResponseException {
        try {
            return authDAO.get(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void delete(String authToken) throws ResponseException {
        authDAO.delete(authToken);
    }

    public void deleteAll() throws ResponseException {
        authDAO.deleteAll();
    }
}