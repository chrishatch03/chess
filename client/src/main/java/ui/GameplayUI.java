package ui;

import java.util.Arrays;

import chess.ChessBoard;
import chess.ChessGame;
import exception.*;
import model.*;
import ui.websocket.WebSocketFacade;

public class GameplayUI {
    private final ServerFacade server;
    private final Repl repl;
    private WebSocketFacade ws;


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
                case "redraw" -> drawBoard(this.repl.getPlayerColor(), this.repl.getCurrentGame().game().getBoard());
                case "makemove" -> makeMove(params);
                case "highlight" -> highlightLegalMoves(params);
                case "leave" -> leaveGame(params);
                case "resign" -> resign(params);
                case "quit" -> quit();
                case "clear" -> clearApp(params);
                // case "logout" -> logout(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String drawBoard(ChessGame.TeamColor playerColor, ChessBoard board) {
        ChessArt.draw(board, playerColor);
        return "";
    }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 1) {
            System.out.println("Clearing chess");
            return server.clearApp(new EmptyRequest()).toString();
        }
        throw new ResponseException(400, "Expected no params for clear");
    }

    public String highlightLegalMoves(String ... params) throws ResponseException {
        return "";
    }

    public String leaveGame(String... params) throws ResponseException {
        // if (params.length > 0) {
            this.repl.setCurrentGame(null);
            this.repl.setPlayerColor(null);
            return "";
        // }
    
        // throw new ResponseException(400, "Expected no parameters for back.");
    }

    public String makeMove(String... params) throws ResponseException {
        return "";
    }

    public String resign(String... params) throws ResponseException {
        return "";
    }

    public String quit() {
        return "quit";
    }


    public static String help() {
        return """
        
                - redraw
                - makemove
                - highlight
                - help
                - leave
                - resign
                - quit
                - clear
                """;
    }

}
