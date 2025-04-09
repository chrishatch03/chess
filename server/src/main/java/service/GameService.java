package service;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import exception.ResponseException;
import model.JoinGameRequest;
import model.UpdateGameRequest;
import model.UserData;
import java.util.Collection;

import chess.ChessGame;
import chess.ChessMove;

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

            String color = joinGameRequest.playerColor().toLowerCase();
            String newWhiteUsername = gameData.whiteUsername();
            String newBlackUsername = gameData.blackUsername();

            if ("white".equals(color)) {
                if (newWhiteUsername != null) {
                    throw new ResponseException(403, "Error: already taken");
                }
                newWhiteUsername = userData.username();
            } else if ("black".equals(color)) {
                if (newBlackUsername != null) {
                    throw new ResponseException(403, "Error: already taken");
                }
                newBlackUsername = userData.username();
            } else {
                throw new ResponseException(400, "Error: bad request");
            }

            // Perform the update using a common method
            updateGameWithNewPlayer(gameData.gameID(), newWhiteUsername, newBlackUsername, gameData);

            return gameDAO.get(joinGameRequest.gameID());
        } catch (DataAccessException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void updateGameWithNewPlayer(Integer gameID, String whiteUsername, String blackUsername, GameData gameData)
            throws DataAccessException {
        GameData updatedGameData = gameDAO.update(gameID, new GameData(
                gameData.gameID(),
                whiteUsername,
                blackUsername,
                gameData.gameName(),
                gameData.game()
        ));
        if (updatedGameData.game() == null) {
            gameDAO.update(gameID, new GameData(
                    gameData.gameID(),
                    whiteUsername,
                    blackUsername,
                    gameData.gameName(),
                    new ChessGame()
            ));
        }
    }

    public GameData makeMove(String username, String playerColor, Integer gameID, ChessMove move, UserData userData) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(gameID);
            if (gameData == null) {
                throw new ResponseException(500, "Error: game doesn't exist");
            }
            
            ChessGame game = gameData.game();
            if (game == null) {
                throw new ResponseException(500, "Error: game is empty");

            }
            ChessGame.TeamColor turnColor = game.getTeamTurn();

            // is the user in the game
            if (playerColor.equalsIgnoreCase("white")) {
                if (gameData.whiteUsername() == null || !gameData.whiteUsername().equalsIgnoreCase(userData.username())) {
                    throw new ResponseException(403, "Error: bad request - white username doesn't match request playerColor");
                }
                if (turnColor != ChessGame.TeamColor.WHITE) {
                    throw new ResponseException(403, "Error: bad request - Black's turn, wait your turn white");
                }
            } else if (playerColor.equalsIgnoreCase("black")) {
                if (gameData.blackUsername() == null || !gameData.blackUsername().equalsIgnoreCase(userData.username())) {
                    throw new ResponseException(403, "Error: bad request - black username doesn't match request playerColor");
                }
                if (turnColor != ChessGame.TeamColor.BLACK) {
                    throw new ResponseException(403, "Error: bad request - White's turn, wait your turn black");
                }
            } 


            game.makeMove(move);

            GameData updatedGameData = new GameData(
            gameData.gameID(),
            gameData.whiteUsername(),
            gameData.blackUsername(),
            gameData.gameName(),
            game
            );

            return gameDAO.update(gameData.gameID(), updatedGameData);

        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    
    public GameData updateGame(UpdateGameRequest request, UserData userData) throws ResponseException {
        try {
            GameData gameData = gameDAO.get(request.gameID());
            if (gameData == null) {
                throw new ResponseException(500, "Error: game doesn't exist");
            }
            // is the user in the game
            if (request.playerColor().toLowerCase().equals("white")) {
                if (gameData.whiteUsername() == null || !gameData.whiteUsername().equalsIgnoreCase(userData.username())) {
                    throw new ResponseException(403, "Error: bad request - white username doesn't match request playerColor");
                }
            } else if (request.playerColor().toLowerCase().equals("black")) {
                if (gameData.blackUsername() == null || !gameData.blackUsername().equalsIgnoreCase(userData.username())) {
                    throw new ResponseException(403, "Error: bad request - black username doesn't match request playerColor");
                }
            } 
            

            return gameDAO.update(gameData.gameID(), request.game());

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