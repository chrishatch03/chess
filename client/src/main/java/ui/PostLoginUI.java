package ui;

import static ui.EscapeSequences.*;

import java.util.Arrays;
import chess.ChessGame;
import exception.*;
import server.ServerFacade;
import model.*;

public class PostLoginUI {
    private final ServerFacade server;
    private final Repl repl;

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
            server.logout(repl.getAuthToken());
            this.repl.setAuthToken(null);
            this.repl.setUsername(null);
            return "Logging you out...";
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
                output.append("Game ").append(currentGame.gameID().toString()).append(": ").append(currentGame.gameName());

                String status = getStatus(currentGame);

                output.append("  -->   Available: ").append(status).append("\n");
            }
            return output.toString();
        }
        throw new ResponseException(400, "Expected no params for 'list'");
    }

    private static String getStatus(GameData currentGame) {
        String status;
        boolean whiteAvailable = currentGame.whiteUsername() == null || currentGame.whiteUsername().isEmpty();
        boolean blackAvailable = currentGame.blackUsername() == null || currentGame.blackUsername().isEmpty();
        if (whiteAvailable && blackAvailable) { status = "white, black"; }
        else if (whiteAvailable) { status = "white"; }
        else if (blackAvailable) { status = "black"; }
        else { status = "full"; }
        return status;
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var playerColor = params[0];
            var games = server.listGames(repl.getAuthToken()).games();
            String gameName = String.valueOf(params[1]).trim().toLowerCase();
            Integer gameId = null;
            for (GameData game : games) {
                if (game.gameName().toLowerCase().equals(gameName)) {
                    gameId = game.gameID();
                }
            }
            if (gameId == null) { throw new ResponseException(400, "Could not find the game you tried to join"); }

            var truePlayerColor = stringToPlayerColor(playerColor);
            if (truePlayerColor == null) { throw new ResponseException(400, "Could not convert " + playerColor + " to ChessGame.TeamColor"); }
            GameData alreadyJoinedExistingGame = rejoinGame(truePlayerColor, gameId, this.repl.getUsername());
            if (alreadyJoinedExistingGame != null) {
                this.repl.setCurrentGame(alreadyJoinedExistingGame);
            }

            server.joinGame(new JoinGameRequest(playerColor, gameId), repl.getAuthToken()).toString();
            GameData newGame = getGame(gameId);
            this.repl.setCurrentGame(newGame);
            this.repl.setPlayerColor(playerColor);
            return "";
        }
        throw new ResponseException(400, "Expected: <playerColor> <gameId>");
    }

    public GameData getGame(Integer gameId) {
        try {
            var games = server.listGames(repl.getAuthToken()).games();
            for (GameData currentGame : games) {
                if (currentGame.gameID().equals(gameId)) {
                    return currentGame;
                }
            }
            return null;
        } catch (Exception exception) {
            System.out.println("getGame(): unable to get game " + gameId);
            return null;
        }
    }

    public GameData rejoinGame(ChessGame.TeamColor ignoredPlayerColor, Integer gameId, String userUsername) {
        
        GameData game = getGame(gameId);
        if (game == null) { return null; }

        if (game.whiteUsername() != null && game.whiteUsername().equals(userUsername)) { return game; }
        if (game.blackUsername() != null && game.blackUsername().equals(userUsername)) { return game; }

        return null;
    }

    public ChessGame.TeamColor stringToPlayerColor(String playerColorString) {
        String cleanString = playerColorString.trim().toLowerCase();

        if (cleanString.equals("white")) { return ChessGame.TeamColor.WHITE; }
        else if (cleanString.equals("black")) { return ChessGame.TeamColor.BLACK; }
        return null;
    }

    public String clearApp(String... params) throws ResponseException {
        if (params.length == 0) {
            System.out.println("Clearing chess");
            server.clearApp(new EmptyRequest());
            this.repl.setAuthToken(null);
            this.repl.setUsername(null);
            this.repl.setCurrentGame(null);
            this.repl.setPlayerColor(null);
            return "Database cleared, logging you out...";
        }
        throw new ResponseException(400, "Expected no params for clear");
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
