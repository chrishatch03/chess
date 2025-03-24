package service;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import exception.ResponseException;
import model.JoinGameRequest;
import model.UserData;
import java.util.Collection;

public class GameService {

    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public GameData add(String gameName) throws ResponseException {
        try {
            return gameDAO.add(gameName);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public GameData joinGame(JoinGameRequest joinGameRequest, UserData userData) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(joinGameRequest.gameID());
            if (gameData == null) {
                throw new ResponseException(500, "Error: game doesn't exist");
            }
            if (joinGameRequest.playerColor().toLowerCase().equals("white")) {
                if (gameData.whiteUsername() != null) {
                    throw new ResponseException(403, "Error: already taken");
                }
                return gameDAO.update(gameData.gameID(), new GameData(gameData.gameID(),
                        userData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game()));
            } else if (joinGameRequest.playerColor().toLowerCase().equals("black")) {
                if (gameData.blackUsername() != null) {
                    throw new ResponseException(403, "Error: already taken");
                }
                return gameDAO.update(gameData.gameID(), new GameData(gameData.gameID(),
                        gameData.whiteUsername(), userData.username(), gameData.gameName(), gameData.game()));
            } else {
                throw new ResponseException(400, "Error: bad request");
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public Collection<GameData> listAll() throws ResponseException {
        try {
            return gameDAO.listAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public GameData get(Integer gameID) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(gameID);
            if (gameData == null) {
                throw new ResponseException(500, "Error: game doesn't exist");
            }
            return gameDAO.get(gameID);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void delete(Integer gameID) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(gameID);
            if (gameData == null) {
                throw new ResponseException(500, "Error: game doesn't exist");
            }
            gameDAO.delete(gameID);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void deleteAll() throws ResponseException {
        try {
            gameDAO.deleteAll();
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}