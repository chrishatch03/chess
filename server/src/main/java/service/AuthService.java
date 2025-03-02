package service;
import dataaccess.AuthMemoryDAO;
import model.AuthData;
import exception.ResponseException;
import java.util.Collection;

public class AuthService {

    private final AuthMemoryDAO authDAO;

    public AuthService(AuthMemoryDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData addAuthData(AuthData authData) throws ResponseException {
        if ( authDAO.get(authData.username()) == null ) {
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return authDAO.add(authData);
    }

    public Collection<AuthData> listAllAuthData() throws ResponseException {
        return authDAO.listAll();
    }

    public AuthData getAuthData(String username) throws ResponseException {
        return authDAO.get(username);
    }

    public void deleteAuthData(String username) throws ResponseException {
        authDAO.delete(username);
    }

    public void deleteAllAuthData() throws ResponseException {
        authDAO.deleteAll();
    }
}