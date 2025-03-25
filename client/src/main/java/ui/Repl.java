package ui;

import java.util.Scanner;

import chess.*;
import model.*;

import static ui.EscapeSequences.*;

public class Repl {
    
    private final PreLoginUI preLoginClient;
    private final PostLoginUI postLoginClient;
    private final GameplayUI gameplayClient;

    private String authToken = null;
    private String username = null;
    private GameData currentGame = null;
    private ChessGame.TeamColor playerColor = null;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginUI(serverUrl, this);
        postLoginClient = new PostLoginUI(serverUrl, this);
        gameplayClient = new GameplayUI(serverUrl, this);

    }

    public void run() {
        System.out.println("Welcome to chess. Register or sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            if (currentGame != null) {
                ChessBoard blankBoard = new ChessBoard();
                blankBoard.resetBoard();
                gameplayClient.drawBoard(playerColor, blankBoard);
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
                if (result != "" && !result.isEmpty()) {
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

    public void setPlayerColor(String newPlayerColor) {
        if (newPlayerColor == null) {
            this.playerColor = null;
        } else if (newPlayerColor.trim().toLowerCase().equals("white")) {
            this.playerColor = ChessGame.TeamColor.WHITE;
        } else {
            this.playerColor = ChessGame.TeamColor.BLACK;
        }
    }

    private void printPrompt() {
        String authState = (authToken == null || authToken.isEmpty()) ? "[Logged Out] " : "[Logged In] ";
        System.out.print("\n" + RESET_BG_COLOR + SET_TEXT_COLOR_BLACK + authState + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
