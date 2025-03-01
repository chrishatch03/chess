package service;
import dataaccess.DataAccess;
import model.UserData;
import model.UserDataType;
import exception.ResponseException;
import java.util.Collection;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserData addUserData(UserData userData) throws ResponseException {
        if (userData.type() == UserDataType.DOG && userData.name().equals("fleas")) {
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return dataAccess.addUserData(userData);
    }

    public Collection<UserData> listAllUserData() throws ResponseException {
        return dataAccess.listAllUserData();
    }

    public UserData getUserData(int id) throws ResponseException {
        return dataAccess.getUserData(id);
    }

    public void deleteUserData(Integer id) throws ResponseException {
        dataAccess.deleteUserData(id);
    }

    public void deleteAllUserData() throws ResponseException {
        dataAccess.deleteAllUserData();
    }
}