package ui;

// import java.io.CharArrayReader;
import java.util.Arrays;

import chess.ChessBoard;
import exception.*;
import server.ServerFacade;
import model.*;
// import static ui.EscapeSequences.*;

public class GameplayUI {
    private final ServerFacade server;
    private String playerColor = null;
    private Repl repl;

    // private String BORDER_BG_COLOR = SET_BG_COLOR_YELLOW;
    // private String BORDER_TEXT_COLOR = SET_TEXT_COLOR_BLACK;

    // private String BLACK_PIECE_COLOR = SET_TEXT_COLOR_BLUE;
    // private String BLACK_BG_COLOR = SET_BG_COLOR_BLACK;

    // private String WHITE_PIECE_COLOR = SET_TEXT_COLOR_RED;
    // private String WHITE_BG_COLOR = SET_BG_COLOR_DARK_GREY;



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
                // case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public void drawBoard(String playerColor, ChessBoard board) {
        ChessArt.draw(board);
    }

    // public void drawPerspective(ChessBoard board, String textColor, String bgColor, int startRow, int rowInc, int startCol, int colInc, String[] colTitles, String[] rowTitles) {

    // }

    // public String buildSquare(char characterToPrint, String textColor, String bgColor) {
    //     return bgColor + textColor + "   \n " + characterToPrint + " \n   ";
    // }

    // public String buildRow(String textColor, String bgColor) {
    //     StringBuilder row = new StringBuilder();
    //     char[] rowChars = new char[]{' ', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', ' '};
    //     for (int i = 0; i < 10; i++) {
    //         row.append(buildSquare( rowChars[i], textColor, bgColor));
    //     }
    //     return row.toString(); 
    // }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 1) {
            System.out.println("Clearing chess");
            return server.clearApp(new EmptyRequest()).toString();
        }
        throw new ResponseException(400, "Expected no params for clear");
    }

    // public String logout(String... params) throws ResponseException {
    //     this.repl.logout();
    // }

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
