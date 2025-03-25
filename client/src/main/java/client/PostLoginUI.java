package client;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

import java.util.Arrays;
import exception.*;
import server.ServerFacade;
import model.*;
import static ui.EscapeSequences.*;

public class PostLoginUI {
    private final ServerFacade server;
    private String playerColor = null;
    private Repl repl;

    public PostLoginUI(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "clear" -> clearApp(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    public String logout(String... params) throws ResponseException {
        if (params.length == 0) {
            System.out.println("Logging out: username=" + repl.getUsername());
            Object res = server.logout(repl.getAuthToken());
            this.repl.setAuthToken("");
            this.repl.setUsername("");
            return res.toString();
        }
        throw new ResponseException(400, "Expected no parameters for logout");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            var gameName = params[0];
            return "Created game: " + server.createGame(new CreateGameRequest(gameName), repl.getAuthToken()).gameID().toString();
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String listGames(String... params) throws ResponseException {
        if (params.length == 0) {
            var games = server.listGames(repl.getAuthToken()).games();
            StringBuilder output = new StringBuilder(SET_TEXT_COLOR_WHITE + "Games Available:\n");
            for (GameData currentGame : games) {
                output.append(currentGame.toString() + "\n");
            }
            return output.toString();
        }
        throw new ResponseException(400, "Expected no params for 'list'");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var playerColor = params[0];
            var gameId = Integer.parseInt(params[1]);
            System.out.println("Joining game: " + gameId);
            server.joinGame(new JoinGameRequest(playerColor, gameId), repl.getAuthToken()).toString();
            this.repl.setCurrentGame(gameId);
            this.repl.setPlayerColor(playerColor);
            return "";
        }
        throw new ResponseException(400, "Expected: <playerColor> <gameId>");
    }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 0) {
            System.out.println("Clearing chess");
            return server.clearApp(new EmptyRequest()).toString();
        }
        throw new ResponseException(400, "Expected no params for clear");
    }

    public String getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayercolor(String newPlayerColor) {
        this.playerColor = newPlayerColor;
    }


    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                - list
                - create <gameName>
                - join <playerColor> <gameId>
                - help
                - logout
                - clear
                - quit
                """;
    }

}
