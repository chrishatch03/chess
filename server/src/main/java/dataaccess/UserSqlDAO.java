package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserSqlDAO implements UserDAO {

    public UserSqlDAO() throws DataAccessException {
        DataAccess.configureDatabase(createStatements);
    }

    @Override
    public UserData add(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var checkStatement = "SELECT username FROM userData WHERE username = ?";
            try (var ps = conn.prepareStatement(checkStatement)) {
                ps.setString(1, userData.username());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        throw new DataAccessException("Error: already taken");
                    }
                }
            }
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            userData = new UserData(userData.username(), hashedPassword, userData.email());
            var insertStatement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
            var json = new Gson().toJson(userData);
            DataAccess.executeUpdate(insertStatement, userData.username(), hashedPassword, userData.email(), json);
            return userData;
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to add user: %s", e.getMessage()));
        }
    }

    @Override
    public Collection<UserData> listAll() throws DataAccessException {
        var result = new ArrayList<UserData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM userData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readUser(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public UserData get(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM userData WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    } else {
                        throw new DataAccessException("Error: " + username + " not found in userDb");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public void delete(String username) throws DataAccessException {
        var statement = "DELETE FROM userData WHERE username=?";
        DataAccess.executeUpdate(statement, username);
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE userData";
        DataAccess.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        rs.getString("username");
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
