package server.websocket;

import com.google.gson.Gson;
import chess.ChessGame;
import chess.ChessMove;
import exception.ResponseException;
import model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;
import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import service.*;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    public Set<Integer> finishedGames = java.util.Collections.synchronizedSet(new HashSet<>());
    private final AuthService authService;
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(AuthService authService, UserService userService, GameService gameService) {
        this.authService = authService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try {
            var jsonObject = new Gson().fromJson(message, com.google.gson.JsonObject.class);
            var commandType = UserGameCommand.CommandType.valueOf(jsonObject.get("commandType").getAsString());

            UserGameCommand command;
            switch (commandType) {
                case MAKE_MOVE -> {
                    command = new Gson().fromJson(message, MoveCommand.class);
                    makeMove((MoveCommand) command);
                }
                case CONNECT -> {
                    command = new Gson().fromJson(message, UserGameCommand.class);
                    connect(command, session);
                }
                case LEAVE -> {
                    command = new Gson().fromJson(message, UserGameCommand.class);
                    leave(command);
                }
                case RESIGN -> {
                    command = new Gson().fromJson(message, UserGameCommand.class);
                    resign(command, session);
                }
                default -> throw new ResponseException(400, "Unknown command type");
            }
        } catch (Exception ex) {
            sendErrorMessage(session, ex.getMessage());
        }
    }

    private void sendErrorMessage(Session session, String errorMessage) {
        try {
            ServerMessage errorNotification = new WebSocketError(ServerMessage.ServerMessageType.ERROR, errorMessage);
    
            if (session.isOpen()) {
                session.getRemote().sendString(new Gson().toJson(errorNotification));
            }
        } catch (IOException e) {
            System.err.println("Failed to send error message: " + e.getMessage());
        }
    }

    private void connect(UserGameCommand command, Session session) {
        AuthData authData;
        GameData game;
        try {
            authData = authService.sessionExists(command.getAuthToken());
            game = gameService.get(command.getGameID());
            connections.add(command.getAuthToken(), session, command.getGameID());
            LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
            connections.broadcastIndividual(command.getAuthToken(), loadGameMessage);
        } catch (Exception ex) {
            sendErrorMessage(session, "Error: couldn't connect to game");
            return;
        }
    
        try {
            String role;
            if (authData.username().equals(game.whiteUsername())) {
                role = " (white)";
            } else if (authData.username().equals(game.blackUsername())) {
                role = " (black)";
            } else {
                role = " (observer)";
            }
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, 
                    authData.username() + role + " joined");
            connections.broadcastExclude(command.getAuthToken(), command.getGameID(), notification);
        } catch (Exception e) {
            System.err.println("Warning: failed to send join notification: " + e.getMessage());
        }
    }
    
    private void leave(UserGameCommand command) throws ResponseException {
        AuthData authData = authService.sessionExists(command.getAuthToken());
        UserData userData = userService.get(authData.username());
        GameData gameData = gameService.get(command.getGameID());
    
        String playerColor;
        if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(authData.username())) {
            playerColor = "white";
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(authData.username())) {
            playerColor = "black";
        } else {
            connections.remove(command.getAuthToken());
            Notification notification =
                    new Notification(ServerMessage.ServerMessageType.NOTIFICATION,
                            "Observer " + authData.username() + " left the game");
            connections.broadcastExclude(command.getAuthToken(), command.getGameID(), notification);
            return;
        }
    
        if (playerColor.equals("white")) {
            gameService.updateGame(new UpdateGameRequest(playerColor, command.getGameID(),
                new GameData(command.getGameID(), null, gameData.blackUsername(), gameData.gameName(),
                        gameData.game())), userData);
        } else {
            gameService.updateGame(new UpdateGameRequest(playerColor, command.getGameID(),
                new GameData(command.getGameID(), gameData.whiteUsername(), null, gameData.gameName(),
                        gameData.game())), userData);
        }
    
        connections.remove(command.getAuthToken());
        Notification notification =
                new Notification(ServerMessage.ServerMessageType.NOTIFICATION,
                        "Player " + authData.username() + " left the game");
        connections.broadcastExclude(command.getAuthToken(), command.getGameID(), notification);
    }
    
    private void makeMove(MoveCommand command) throws ResponseException {
        var authToken = command.getAuthToken();
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        Integer gameID = command.getGameID();
        if (gameID == null) {
            throw new ResponseException(400, "Error: bad request - invalid game ID");
        }

        if (finishedGames.contains(command.getGameID())) {
            connections.broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.ENDGAME));
            throw new ResponseException(400, "Error: game is over");
        }

        var authData = authService.sessionExists(authToken);
        var userData = userService.get(authData.username());
        GameData game = gameService.get(command.getGameID());
        
        String playerColor;
        if (authData.username().equals(game.whiteUsername())) { playerColor = "white"; }
        else if (authData.username().equals(game.blackUsername())) { playerColor = "black"; }
        else { throw new ResponseException(403, "Error: bad request - invalid TeamColor "); }

        if (playerColor == null || !playerColor.equalsIgnoreCase("white") &&
                !playerColor.equalsIgnoreCase("black")) {
            throw new ResponseException(403, "Error: bad request - invalid TeamColor " + playerColor);
        }

        ChessGame.TeamColor playerTeamColor;
        if (playerColor.equalsIgnoreCase("white")) {playerTeamColor = ChessGame.TeamColor.WHITE;}
        else if (playerColor.equalsIgnoreCase("black")) {playerTeamColor = ChessGame.TeamColor.BLACK;}
        else { throw new ResponseException(500, "Error: somehow the playerColor still is different than white/black"); }
        
        if (game.game().isInCheckmate(playerTeamColor)) {
            connections.broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.ENDGAME));
            throw new ResponseException(400, "Game is over, your opponent won");
        }

        Collection<ChessMove> validMoves = game.game().validMoves(command.move.getStartPosition());
        if (!validMoves.contains(command.move)) {
            if (game.game().isInCheck(playerTeamColor)) { throw new ResponseException(400, "Error: Invalid move, you're in check"); }
            throw new ResponseException(400, "Error: invalid Move");
        }

        GameData newGameData = gameService.makeMove(authData.username(),
                playerColor, command.getGameID(), command.move, userData);
        
        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, newGameData);
        connections.broadcastToGame(command.getGameID(), loadMessage);

        String whiteInCheckmate = "";
        String blackInCheckmate = "";
        String whiteInCheck = "";
        String blackInCheck = "";

        if (newGameData.game().isInCheckmate(ChessGame.TeamColor.WHITE) || newGameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            if (newGameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
                whiteInCheckmate = "\nCheckmate! Black wins";
                finishedGames.add(command.getGameID());
                connections.broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.ENDGAME));
            } else if (newGameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                blackInCheckmate = "\nCheckmate! White wins";
                finishedGames.add(command.getGameID());
                connections.broadcastToGame(command.getGameID(), new ServerMessage(ServerMessage.ServerMessageType.ENDGAME));
            }
            var notification = new
                    Notification(ServerMessageType.NOTIFICATION,
                    authData.username() + " moved from " + printMove(command.move) + whiteInCheckmate + blackInCheckmate);
            connections.broadcastToGame(command.getGameID(), notification);
        } else if (newGameData.game().isInCheck(ChessGame.TeamColor.WHITE) || newGameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            if (newGameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
                whiteInCheck = "\nWhite is in check";
            } else if (newGameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
                blackInCheck = "\nBlack is in check";
            }
            var notification = new
                    Notification(ServerMessageType.NOTIFICATION,
                    authData.username() + " moved from " + printMove(command.move) + whiteInCheck + blackInCheck);
            connections.broadcastToGame(command.getGameID(), notification);
        } else {
            var notification = new
                    Notification(ServerMessageType.NOTIFICATION,
                    authData.username() + " moved from " + printMove(command.move));
            connections.broadcastExclude(command.getAuthToken(), command.getGameID(), notification);
        }

    }

    private String printMove(ChessMove move) {
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();


        StringBuilder builder = new StringBuilder();
        builder.append(intToString(startCol));
        builder.append(String.valueOf(startRow));
        builder.append(intToString(endCol));
        builder.append(String.valueOf(endRow));
        
        return builder.toString();
    }

    private String intToString(int num) {
        switch (num) {
            case 1 : return "a";
            case 2 : return "b";
            case 3 : return "c";
            case 4 : return "d";
            case 5 : return "e";
            case 6 : return "f";
            case 7 : return "g";
            case 8 : return "h";
        }
        return "";
    }
    
    private void resign(UserGameCommand command, Session session) throws ResponseException {
        AuthData authData = authService.sessionExists(command.getAuthToken());
        GameData gameData = gameService.get(command.getGameID());
    
        if (!authData.username().equals(gameData.whiteUsername()) &&
            !authData.username().equals(gameData.blackUsername())) {
            throw new ResponseException(400, "Error: requester cannot resign from game because they are a silly observer");
        }
    
         // Check if already over
         if (finishedGames.contains(command.getGameID())) {
             throw new ResponseException(400, "Error: game is over");
         }
    
        finishedGames.add(command.getGameID());
        Notification resignNote = new Notification(
            ServerMessage.ServerMessageType.NOTIFICATION,
            "player " + authData.username() + " resigned, game is over"
        );
        connections.broadcastToGame(command.getGameID(), resignNote);
    }

}
