package ui;

import java.util.Arrays;
import chess.ChessBoard;
import chess.ChessGame;
import exception.*;
import server.ServerFacade;
import model.*;

public class GameplayUI {
    private final ServerFacade server;
    private String playerColor = null;
    private Repl repl;


    public GameplayUI(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "clear" -> clearApp(params);
                case "back" -> backToPostLogin(params);
                // case "logout" -> logout(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public void drawBoard(ChessGame.TeamColor playerColor, ChessBoard board) {
        ChessArt.draw(board, playerColor);
    }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 1) {
            System.out.println("Clearing chess");
            return server.clearApp(new EmptyRequest()).toString();
        }
        throw new ResponseException(400, "Expected no params for clear");
    }

    public String backToPostLogin(String... params) {
        this.repl.setCurrentGame(null);
        this.repl.setPlayerColor(null);
        return "";
    }

    public String getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayercolor(String newPlayerColor) {
        this.playerColor = newPlayerColor;
    }


    public String help() {
        return """
                - commands to come in phase 6
                - help
                - logout
                - clear
                - quit
                """;
    }

}
