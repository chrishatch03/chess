package ui;

import java.util.Objects;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import chess.*;
import exception.ResponseException;
import model.*;
import ui.websocket.ServerMessageHandler;
import ui.websocket.WebSocketFacade;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.messages.*;
import websocket.messages.WebSocketError;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
    
    private final PreLoginUI preLoginClient;
    private final PostLoginUI postLoginClient;
    private final GameplayUI gameplayClient;
    private final WebSocketFacade ws;

    private String authToken = null;
    private String username = null;
    private GameData currentGame = null;
    private ChessGame.TeamColor playerColor = null;
    private boolean observer = false;
    public boolean highlight = false;

    public enum GameValues {
        OBSERVER
    };

    public Repl(String serverUrl) throws ResponseException {

        try {
            ws = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException exception) {
            throw new ResponseException(500, "Error: could not configure websocketServerFacade");
        }
        preLoginClient = new PreLoginUI(serverUrl, this);
        postLoginClient = new PostLoginUI(serverUrl, this, ws);
        gameplayClient = new GameplayUI(serverUrl, this, ws);
    }

    public void run() {
        System.out.println("Welcome to chess. Register or sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.trim().equals("quit")) {
            if (currentGame != null) {
                if (highlight == false) {
                    gameplayClient.drawBoard(playerColor, currentGame.game().getBoard());
                } else {
                    highlight = false;
                }
                if (observer == true) { System.out.print(GameplayUI.help("observer")); }
                else { System.out.print(GameplayUI.help("player")); }
            }
            printPrompt();
            String userInput = scanner.nextLine();
            System.out.print(SET_TEXT_COLOR_BLACK);
            try {
                if (authToken == null || authToken.isEmpty()) {
                    result = preLoginClient.eval(userInput);
                } else {
                    if (currentGame == null) {
                        result = postLoginClient.eval(userInput);
                    } else {
                        result = gameplayClient.eval(userInput);
                    }
                }
                if (!Objects.equals(result, "") && !result.isEmpty()) {
                    System.out.print(result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        scanner.close();
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String newAuthToken) {
        this.authToken = newAuthToken;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public GameData getCurrentGame() {
        return this.currentGame;
    }

    public void setCurrentGame(GameData newGame) {
        this.currentGame = newGame;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return this.playerColor;
    }

    public boolean isObserver() {
        return this.observer;
    }

    public void setObserver(boolean value) {
        this.observer = value;
    }

    public void setPlayerColor(String newPlayerColor) {
        if (newPlayerColor == null) {
            this.playerColor = null;
        } else if (newPlayerColor.trim().equalsIgnoreCase("white")) {
            this.playerColor = ChessGame.TeamColor.WHITE;
        } else {
            this.playerColor = ChessGame.TeamColor.BLACK;
        }
    }

    private void printPrompt() {
        String authState = (authToken == null || authToken.isEmpty()) ? "[Logged Out] " : "[Logged In] ";
        System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_BLACK + authState + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public void notify(JsonObject message) {
        try {
            var jsonObject = new Gson().fromJson(message, com.google.gson.JsonObject.class);
            var commandType = ServerMessage.ServerMessageType.valueOf(jsonObject.get("serverMessageType").getAsString());
            switch (commandType) {
                case LOAD_GAME -> {
                    this.currentGame = new Gson().fromJson(message, LoadGameMessage.class).game;
                    displayMessage("","");
                }
                case ERROR -> {
                    displayMessage(SET_TEXT_COLOR_RED, new Gson().fromJson(message, WebSocketError.class).errorMessage);
                    
                }
                case NOTIFICATION -> {
                    displayMessage(SET_TEXT_COLOR_BLUE, new Gson().fromJson(message, Notification.class).message);

                }
                default -> throw new ResponseException(400, "Unknown command type");
            }
        } catch (Exception ex) {
            System.out.println(SET_TEXT_COLOR_RED + ex.getMessage());
        }
    }

    public void displayMessage(String textColor, String message) {
        gameplayClient.drawBoard(playerColor, currentGame.game().getBoard());
        if (!message.isEmpty() || message != "") { 
            System.out.println(textColor + message + RESET_TEXT_COLOR);
        } 
        if (observer == true) { System.out.print(GameplayUI.help("observer")); }
        else { System.out.print(GameplayUI.help("player")); }
        printPrompt();
    }

}
