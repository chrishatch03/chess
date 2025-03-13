package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class GameSqlDAO implements GameDAO {

    public GameSqlDAO() throws DataAccessException {
        DataAccess.configureDatabase(createStatements);
    }

    @Override
    public GameData add(String gameName) throws DataAccessException {
        if (gameNameExists(gameName)) {
            throw new DataAccessException("Game name already taken");
        }
        var insertStatement = "INSERT INTO gameData (gameName, json) VALUES (?, ?)";
        int gameID = DataAccess.executeUpdate(insertStatement, gameName, "{}");
        GameData gameData = new GameData(gameID, null, null, gameName, null);
        String fullJson = new Gson().toJson(gameData);
        var updateStatement = "UPDATE gameData SET json = ? WHERE gameID = ?";
        DataAccess.executeUpdate(updateStatement, fullJson, gameID);
        return gameData;
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
        var json = new Gson().toJson(newGameData);
        var statement = "UPDATE gameData SET json=? WHERE gameID=?";
        DataAccess.executeUpdate(statement, json, gameID);
        return newGameData;
    }

    @Override
    public void delete(Integer gameID) throws DataAccessException {
        var statement = "DELETE FROM gameData WHERE gameID=?";
        DataAccess.executeUpdate(statement, gameID);
    }

    @Override
    public void deleteAll() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        DataAccess.executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        rs.getInt("gameID");
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
              `gameID` INT AUTO_INCREMENT NOT NULL,
              `gameName` VARCHAR(256) NOT NULL UNIQUE,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
