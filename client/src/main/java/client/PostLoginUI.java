package client;

import java.util.Arrays;
import exception.*;
import server.ServerFacade;
import model.*;

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
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    public String logout(String... params) throws ResponseException {
        if (params.length == 1) {
            System.out.println("Logging out: username=" + repl.getUsername());
            Object res = server.logout(new EmptyRequest());
            this.repl.setAuthToken("");
            this.repl.setUsername("");
            return res.toString();
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var gameName = params[1];
            System.out.println("Creating game: " + gameName);
            return server.createGame(new CreateGameRequest(gameName)).toString();
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String listGames(String... params) throws ResponseException {
        if (params.length == 1) {
            System.out.println("Getting games");
            return server.listGames(new EmptyRequest()).toString();
        }
        throw new ResponseException(400, "Expected no params for 'list'");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 3) {
            var playerColor = params[1];
            var gameId = Integer.parseInt(params[1]);
            System.out.println("Joining game: " + gameId);
            return server.joinGame(new JoinGameRequest(playerColor, gameId)).toString();
        }
        throw new ResponseException(400, "Expected: <playerColor> <gameId>");
    }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 1) {
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
        return """
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
