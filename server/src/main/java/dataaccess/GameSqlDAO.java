package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class GameSqlDAO implements GameDAO {

    public GameSqlDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public GameData add(String gameName) throws DataAccessException {
        if (gameNameExists(gameName)) {
            throw new DataAccessException("Game name already taken");
        }
        var statement = "INSERT INTO gameData (gameName, game, json) VALUES (?, ?, ?)";
        String fullJson = new Gson().toJson(new GameData(null, null, null, gameName, null));
        int gameID = executeUpdate(statement, gameName, null, fullJson);
        return new GameData(gameID, null, null, gameName, null);
    }

    private boolean gameNameExists(String gameName) throws DataAccessException {
        var statement = "SELECT COUNT(*) FROM gameData WHERE gameName=?";
        try (var conn = DatabaseManager.getConnection();
             var ps = conn.prepareStatement(statement)) {
            ps.setString(1, gameName);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting game count: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Collection<GameData> listAll() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData get(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, json FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public GameData update(Integer gameID, GameData newGameData) throws DataAccessException {
        var statement = "UPDATE gameData SET whiteUsername=?, blackUsername=?, gameName=?, game=?, json=? WHERE gameID=?";
        var json = new Gson().toJson(newGameData);
        System.out.println(json);
        executeUpdate(statement, gameID, newGameData.whiteUsername(), newGameData.blackUsername(), newGameData.gameName(), newGameData.game(), json);
        return newGameData;
    }

    @Override
    public void delete(Integer gameID) throws DataAccessException {
        var statement = "DELETE FROM gameData WHERE gameID=?";
        executeUpdate(statement, gameID);
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
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
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` INT AUTO_INCREMENT NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL UNIQUE,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
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
