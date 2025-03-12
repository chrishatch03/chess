package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class UserSqlDAO implements UserDAO {

    public UserSqlDAO() throws DataAccessException {
        configureDatabase();
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
            executeUpdate(insertStatement, userData.username(), hashedPassword, userData.email(), json);
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
        executeUpdate(statement, username);
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE userData";
        executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var id = rs.getString("username");
        var json = rs.getString("json");
        UserData userData = new Gson().fromJson(json, UserData.class);
        return userData;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update user database: %s, %s", statement, e.getMessage()));
        }
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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure user database: %s", ex.getMessage()));
        }
    }

}
