package ui.websocket;

import com.google.gson.JsonObject;

public interface ServerMessageHandler {
    void notify(JsonObject serverMessage);
}
