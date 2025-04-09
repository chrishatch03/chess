package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import exception.ResponseException;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, Integer gameID) {
        var connection = new Connection(authToken, session, gameID);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcastToGame(Integer gameID, ServerMessage notification) throws ResponseException {
        try {
            Collection<Connection> removeList = new ArrayList<>();
            for (Connection c: connections.values()) {
                if (c.session.isOpen()) {
                    if (c.gameID.equals(gameID)) {
                        c.send(new Gson().toJson(notification));
                    }
                } else {
                    removeList.add(c);
                }
            }

            for (Connection c : removeList) {
                connections.remove(c.authToken);
            }
        } catch (Exception ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }

    public void broadcastExclude(String excludeAuthToken, Integer gameID, ServerMessage notification) throws ResponseException {
        try {
            Collection<Connection> removeList = new ArrayList<>();
            for (Connection c: connections.values()) {
                if (c.session.isOpen()) {
                    if (c.gameID.equals(gameID) && !c.authToken.equals(excludeAuthToken)) {
                        c.send(new Gson().toJson(notification));
                    }
                } else {
                    removeList.add(c);
                }
            }

            for (Connection c : removeList) {
                connections.remove(c.authToken);
            }
        } catch (Exception ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }
    

    public void broadcastIndividual(String authToken, ServerMessage notification) throws ResponseException {
        try {
            Connection c = connections.get(authToken);
            if (c.session.isOpen()) {
                c.send(new Gson().toJson(notification));
            } else {
                connections.remove(c.authToken);
            }
        } catch (Exception ex) {
            throw new ResponseException(500, "Error: " + ex.getMessage());
        }
    }
    
}