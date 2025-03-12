package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import exception.ResponseException;

import java.util.Collection;
import java.util.UUID;

public class AuthService {

    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData add(String username) throws ResponseException {
        try {
            if (username == null) {
                throw new ResponseException(400, "Error: cannot get session for null username");
            }
            return authDAO.add(new AuthData(UUID.randomUUID().toString(), username));
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public Collection<AuthData> listAll() throws ResponseException{
        try {
            return authDAO.listAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public AuthData get(String authToken) throws ResponseException {
        try {
            sessionExists(authToken);
            return authDAO.get(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public AuthData sessionExists(String authToken) throws ResponseException {
        try {
            AuthData authData = authDAO.get(authToken);
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return authData;
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public void delete(String authToken) throws ResponseException {
        try {
            sessionExists(authToken);
            authDAO.delete(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public void deleteAll() throws ResponseException {
        try {
            authDAO.deleteAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }
}