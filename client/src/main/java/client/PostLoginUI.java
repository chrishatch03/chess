package client;

import java.util.Arrays;

import chess.ChessGame;
import exception.*;
import server.ServerFacade;
import model.*;

public class PostLoginUI {
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private final ChessGame.TeamColor playerColor;

    public PostLoginUI(String serverUrl, Repl repl, ChessGame.TeamColor playerColor) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
        this.playerColor = playerColor;
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
        if (params.length == 2) {
            System.out.println("Logging out: username=" + username + " password=" + password);
            return server.logout(new EmptyRequest()).toString();
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
