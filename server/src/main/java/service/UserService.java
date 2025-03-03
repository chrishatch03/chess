package service;
import dataaccess.DataAccessException;
import dataaccess.UserMemoryDAO;
import model.LoginRequest;
import model.UserData;
import exception.ResponseException;
import java.util.Collection;

public class UserService {

    private final UserMemoryDAO userDAO;

    public UserService(UserMemoryDAO userDAO) {
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
        UserData userData = userDAO.get(reqUsername);
        if (userData.username().equals(reqUsername) && userData.password().equals(reqPassword)) {
            return userData;
        } else {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public Collection<UserData> listAll() throws ResponseException {
        return userDAO.listAll();
    }

    public UserData get(String username) throws ResponseException {
        return userDAO.get(username);
    }

    public void delete(String username) throws ResponseException {
        userDAO.delete(username);
    }

    public void deleteAll() throws ResponseException {
        userDAO.deleteAll();
    }
}