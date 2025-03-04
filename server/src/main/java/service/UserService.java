package service;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserMemoryDAO;
import model.LoginRequest;
import model.UserData;
import exception.ResponseException;
import java.util.Collection;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserMemoryDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData register(UserData userData) throws ResponseException {
        try {
            if (userData.username().equals(null) || userData.password().equals(null) || userData.email().equals(null)) {
                throw new ResponseException(400, "Error: bad request");
            }
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
            if (userData.username().equals(reqUsername) && userData.password().equals(reqPassword)) {
                return userData;
            } else {
                throw new ResponseException(401, "Error: unauthorized");
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public Collection<UserData> listAll() {
        return userDAO.listAll();
    }

    public UserData get(String username) throws ResponseException {
        try {
            return userDAO.get(username);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: cannot get UserData for " + username);
        }
    }

    public void delete(String username) {
        userDAO.delete(username);
    }

    public void deleteAll() {
        userDAO.deleteAll();
    }
}