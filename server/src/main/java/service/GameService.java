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

    public GameData add(String gameName) throws ResponseException {
        try {
            return gameDAO.add(gameName);
        } catch (DataAccessException ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public GameData joinGame(JoinGameRequest joinGameRequest, UserData userData) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(joinGameRequest.gameId());
            if (gameData == null) {
                throw new ResponseException(404, "Game not found");
            }
            ChessGame.TeamColor teamColor;
            try {
                teamColor = ChessGame.TeamColor.valueOf(joinGameRequest.teamColor().toString());
            } catch (IllegalArgumentException ex) {
                throw new ResponseException(400, "Invalid team color");
            }
            if (teamColor == ChessGame.TeamColor.WHITE) {
                if (gameData.whiteUsername() != null) {
                    throw new ResponseException(400, "White team is already occupied");
                }
                gameData.setWhiteUsername(userData.username());
            } else if (teamColor == ChessGame.TeamColor.BLACK) {
                if (gameData.blackUsername() != null) {
                    throw new ResponseException(400, "Black team is already occupied");
                }
                gameData.setBlackUsername(userData.username());
            }
            gameDAO.update(gameData.gameId(), gameData);
            return gameData;

        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
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