package ui.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import chess.ChessMove;
import exception.ResponseException;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;


    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                 @Override
                public void onMessage(String message) {
                    JsonObject jsonObject = new Gson().fromJson(message, com.google.gson.JsonObject.class);
                    serverMessageHandler.notify(jsonObject);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectToGame(Integer gameID, String authToken) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leaveGame(String authToken, Integer gameID) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    
    public void makeMove(String authToken, Integer gameID, String playerColor, ChessMove move) throws ResponseException {
        try {
            var userGameCommand = new MoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    
    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}

