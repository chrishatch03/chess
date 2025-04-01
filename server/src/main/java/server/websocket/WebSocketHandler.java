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
            case CONNECT -> connect(command.getAuthToken(), session);
            case LEAVE -> exit(command.getAuthToken());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), new ChessBoard());
            case RESIGN -> resign(command.getAuthToken());
        }
    }

    private void connect(String authToken, Session session) throws IOException {
        connections.add(authToken, session);
        // var message = String.format("%s is in the shop", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(authToken, notification);
    }

    private void exit(String authToken) throws IOException {
        connections.remove(authToken);
        // var message = String.format("%s left the shop", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(authToken, notification);
    }

    private void makeMove(String authToken, ChessBoard newBoard) throws IOException {
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(authToken, notification);
    }

    private void resign(String authToken) throws IOException {
        connections.broadcast(authToken, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
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
