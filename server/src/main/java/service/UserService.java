package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.LoginRequest;
import model.UserData;
import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Collection;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData register(UserData userData) throws ResponseException {
        try {
            return userDAO.add(userData);
        } catch (DataAccessException ex) {
            throw new ResponseException(403, ex.getMessage());
        }
    }

    public UserData verifyCredentials(LoginRequest authCredentials) throws ResponseException {
        String reqUsername = authCredentials.username();
        String reqPassword = authCredentials.password();

        try {
            UserData userData = userDAO.get(reqUsername);
            if (userData != null && BCrypt.checkpw(reqPassword, userData.password())) {
                return userData;
            } else {
                throw new ResponseException(401, "Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public Collection<UserData> listAll() throws ResponseException {
        try {
            return userDAO.listAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public UserData get(String username) throws ResponseException {
        try {
            return userDAO.get(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: cannot get UserData for " + username);
        }
    }

    public void delete(String username) throws ResponseException {
        try {
            userDAO.delete(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public void deleteAll() throws ResponseException {
        try {
            userDAO.deleteAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }
}