package service;
import dataaccess.DataAccess;
import model.AuthData;
import model.AuthDataType;
import exception.ResponseException;
import java.util.Collection;

public class AuthService {

    private final DataAccess dataAccess;

    public AuthService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData addAuthData(AuthData authData) throws ResponseException {
        if (authData.type() == AuthDataType.DOG && authData.name().equals("fleas")) {
            throw new ResponseException(400, "Error: no dogs with fleas");
        }
        return dataAccess.addAuthData(authData);
    }

    public Collection<AuthData> listAllAuthData() throws ResponseException {
        return dataAccess.listAllAuthData();
    }

    public AuthData getAuthData(int id) throws ResponseException {
        return dataAccess.getAuthData(id);
    }

    public void deleteAuthData(Integer id) throws ResponseException {
        dataAccess.deleteAuthData(id);
    }

    public void deleteAllAuthData() throws ResponseException {
        dataAccess.deleteAllAuthData();
    }
}