package server.websocket;

import com.google.gson.Gson;

import chess.ChessBoard;
// import dataaccess.DataAccess;
// import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
// import websocket.messages.ServerMessage.ServerMessageType;

import java.io.IOException;
// import java.util.Timer;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connectToGame(command, session);
            case LEAVE -> exitGame(command.getAuthToken());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), new ChessBoard());
            case RESIGN -> resign(command.getAuthToken());
        }
    }

    private void connectToGame(UserGameCommand command, Session session) throws IOException {
        System.out.println("Message received: CONNECT");
        connections.add(command.getAuthToken(), session, command.getGameID());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastExclude(command.getAuthToken(), notification);
        System.out.println("Message sent: unknown rn is this what websocket messages are for?");
    }

    private void exitGame(String authToken) throws IOException {
        connections.remove(authToken);
        // var message = String.format("%s left the shop", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcastExclude(authToken, notification);
    }

    private void makeMove(String authToken, ChessBoard newBoard) throws IOException {
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastExclude(authToken, notification);
    }

    private void resign(String authToken) throws IOException {
        connections.broadcastExclude(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
    }

    // public void makeNoise(String petName, String sound) throws ResponseException {
    //     try {
    //         var message = String.format("%s says %s", petName, sound);
    //         var notification = new ServerMessage(ServerMessage.ServerMessageType.NOISE, message);
    //         connections.broadcast("", notification);
    //     } catch (Exception ex) {
    //         throw new ResponseException(500, ex.getMessage());
    //     }
    // }
}
