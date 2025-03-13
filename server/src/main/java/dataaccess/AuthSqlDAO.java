package dataaccess;
import model.AuthData;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.*;

public class AuthSqlDAO implements AuthDAO {

    public AuthSqlDAO() throws DataAccessException {
        DataAccess.configureDatabase(createStatements);
    }

    @Override
    public boolean sessionExists(String username) throws DataAccessException {
        var statement = "SELECT username, json FROM authData WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username") != null && !rs.getString("username").isEmpty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return false;
    }

    @Override
    public AuthData add(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authData (authToken, username, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(authData);
        DataAccess.executeUpdate(statement, authData.authToken(), authData.username(), json);
        return new AuthData(authData.authToken(), authData.username());
    }

    @Override
    public Collection<AuthData> listAll() throws DataAccessException {
        var result = new ArrayList<AuthData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM authData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readAuth(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public AuthData get(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, json FROM authData WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void delete(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authData WHERE authToken=?";
        DataAccess.executeUpdate(statement, authToken);
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE authData";
        DataAccess.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        rs.getString("authToken");
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
