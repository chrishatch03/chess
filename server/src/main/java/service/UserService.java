package service;
import dataaccess.DataAccess;
import dataaccess.UserMemoryDAO;
import model.UserData;
import exception.ResponseException;
import java.util.Collection;

public class UserService {

    private final UserMemoryDAO userDAO;

    public UserService(UserMemoryDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserData registerUser(UserData userData) throws ResponseException {
        if ( userDAO.get(userData.username()) != null ) {
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return userDAO.add(userData);
    }

    public Collection<UserData> listAllUserData() throws ResponseException {
        return userDAO.listAll();
    }

    public UserData getUserData(String username) throws ResponseException {
        return userDAO.get(username);
    }

    public void delete(String username) throws ResponseException {
        userDAO.delete(username);
    }

    public void deleteAllUserData() throws ResponseException {
        userDAO.deleteAll();
    }
}