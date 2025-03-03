package service;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameMemoryDAO;
import model.GameData;
import exception.ResponseException;
import model.JoinGameRequest;
import model.UserData;
import java.util.Collection;

public class GameService {

    private final GameDAO gameDAO;

    public GameService(GameMemoryDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public GameData add(String gameName) throws DataAccessException {
        for (GameData game: gameDAO.listAll()) {
            if (game.gameName().equals(gameName)) {
                throw new DataAccessException("Error: game name already taken");
            }
        }
        return gameDAO.add(gameName);
    }

    public GameData joinGame(JoinGameRequest joinGameRequest, UserData userData) throws ResponseException {
        try {
            GameData gameData = this.get(joinGameRequest.gameId());
            if (joinGameRequest.teamColor() == ChessGame.TeamColor.WHITE) {
                gameData.setWhiteUsername(userData.username());
                gameDAO.update(gameData.gameId(), gameData);
            }
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }

        return new GameData(1, "hey", "hiagain", "umm", new ChessGame());
    }

    public Collection<GameData> listAll() {
        return gameDAO.listAll();
    }

    public GameData get(Integer gameId) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(gameId);
            return gameData;
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: no value for " + gameId);
        }
    }

    public void delete(Integer gameId) {
        gameDAO.delete(gameId);
    }

    public void deleteAll() {
        gameDAO.deleteAll();
    }
}