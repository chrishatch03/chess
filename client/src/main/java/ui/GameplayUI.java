package ui;

import java.util.Arrays;
import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.*;
import model.*;
import ui.websocket.WebSocketFacade;
import ui.EscapeSequences.*;

public class GameplayUI {
    private final ServerFacade server;
    private final Repl repl;
    private WebSocketFacade ws;


    public GameplayUI(String serverUrl, Repl repl, WebSocketFacade ws) throws ResponseException {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
        this.ws = ws;
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
                default -> help((this.repl.isObserver() == true) ? "observer" : "player");
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
        if (params.length == 1) {
            ChessBoard board = this.repl.getCurrentGame().game().getBoard();
            if (board == null) { throw new ResponseException(400, "Error: current game is null so could not highlight position"); }

            String moveStr = params[0].toLowerCase();
            if (moveStr.length() != 2) {
                throw new ResponseException(400, "Invalid move format. Expected: makemove <move> where <move> follows format a2a4");
            }
            
            char startColChar = moveStr.charAt(0);
            char startRowChar = moveStr.charAt(1);
            
            if (startColChar < 'a' || startColChar > 'h') {
                throw new ResponseException(400, "Invalid move format: column must be between a and h");
            }
            
            if (startRowChar < '1' || startRowChar > '8') {
                throw new ResponseException(400, "Invalid move format: row must be between 1 and 8");
            }
            
            int startCol = startColChar - 'a' + 1;
            int startRow = startRowChar - '0';
            
            ChessPosition highlightPosition = new ChessPosition(startRow, startCol);
            HighlightChessArt.highlight(board, this.repl.getPlayerColor(), highlightPosition, this.repl.getCurrentGame().game());
            this.repl.highlight = true;
            return "";
        }
        
        throw new ResponseException(400, "Expected: highlight <position>");
    }

    public String leaveGame(String... params) throws ResponseException {
        if (params.length == 0) {
            ws.leaveGame(this.repl.getAuthToken(), this.repl.getCurrentGame().gameID());
            this.repl.setCurrentGame(null);
            this.repl.setPlayerColor(null);
            this.repl.setObserver(false);
            return "";
        }
    
        throw new ResponseException(400, "Expected no parameters for back.");
    }

    public String makeMove(String... params) throws ResponseException {
        if (params.length == 1) {
            String moveStr = params[0].toLowerCase();
            if (moveStr.length() != 4) {
                throw new ResponseException(400, "Invalid move format. Expected: makemove <move> where <move> looks like a2a4");
            }
            
            char startColChar = moveStr.charAt(0);
            char startRowChar = moveStr.charAt(1);
            char endColChar = moveStr.charAt(2);
            char endRowChar = moveStr.charAt(3);
            
            if (startColChar < 'a' || startColChar > 'h' ||
                endColChar < 'a' || endColChar > 'h') {
                throw new ResponseException(400, "Invalid move format: columns must be between a and h");
            }
            
            if (startRowChar < '1' || startRowChar > '8' ||
                endRowChar < '1' || endRowChar > '8') {
                throw new ResponseException(400, "Invalid move format: rows must be between 1 and 8");
            }
            
            int startCol = startColChar - 'a' + 1;
            int endCol = endColChar - 'a' + 1;
            int startRow = startRowChar - '0';
            int endRow = endRowChar - '0';
            
            ChessPosition startPosition = new ChessPosition(startRow, startCol);
            ChessPosition endPosition = new ChessPosition(endRow, endCol);
            
            ChessMove move = new ChessMove(startPosition, endPosition, null);
            
            String playerColor;
            if (this.repl.isObserver()) { throw new ResponseException(400, "Observer cannot make moves"); }
            else if (this.repl.getPlayerColor().equals(ChessGame.TeamColor.WHITE)) { playerColor = "white"; }
            else if (this.repl.getPlayerColor().equals(ChessGame.TeamColor.BLACK)) { playerColor = "black"; }
            else { throw new ResponseException(400, "Error, player color must have been null"); }

            ws.makeMove(this.repl.getAuthToken(), this.repl.getCurrentGame().gameID(), playerColor, move);
            return "";
        }
        throw new ResponseException(400, "Expected: makemove <move> \n<move> could look like this -> a2a4");
    }

    public String resign(String... params) throws ResponseException {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Are you sure you want to resign? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        if (userInput.trim().equalsIgnoreCase("y")) {
            ws.resign(this.repl.getAuthToken(), this.repl.getCurrentGame().gameID());
        }
        return "";
    }

    public String quit() {
        return "quit";
    }


    public static String help(String type) {
        if (type.toLowerCase().equals("observer")) {
            return """

                    - redraw
                    - help
                    - leave
                    """;
        } else {
            return """
    
                    - redraw
                    - makemove <move like unto a2a4>
                    - highlight <position like unto a2>
                    - help
                    - leave
                    - resign
                    """;
        }
    }

}
