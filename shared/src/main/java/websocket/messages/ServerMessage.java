package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    // Don't alter this method
    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }
    
    // Don't alter this method
    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }
    
    // Don't alter this method
    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    
    // Don't alter this method
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }
    
    // Don't alter this method
    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
