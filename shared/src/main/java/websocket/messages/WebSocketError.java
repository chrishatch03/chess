package websocket.messages;

public class WebSocketError extends ServerMessage {

    public String errorMessage;

    public WebSocketError(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
    
}
