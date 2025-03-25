package ui;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

import java.util.Arrays;
import chess.ChessGame;
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
                output.append("Game " + currentGame.gameID().toString() + ": " + currentGame.gameName().toString());

                String status;
                boolean whiteAvailable = (currentGame.whiteUsername() == null || currentGame.whiteUsername().isEmpty()) ? true : false;
                boolean blackAvailable = (currentGame.blackUsername() == null || currentGame.blackUsername().isEmpty()) ? true : false;
                if (whiteAvailable == true && blackAvailable == true) { status = "white, black"; }
                else if (whiteAvailable == true && blackAvailable == false) { status = "white"; }
                else if (whiteAvailable == false && blackAvailable == true) { status = "black"; }
                else { status = "full"; }

                output.append("  -->   Available: " + status + "\n");
            }
            return output.toString();
        }
        throw new ResponseException(400, "Expected no params for 'list'");
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
            if (gameId == null) { throw new ResponseException(400, "Could not find the game you tried to join: " + String.valueOf(gameId)); }

            var truePlayerColor = stringToPlayerColor(playerColor);
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
            System.out.println("getGame(): unable to get game " + String.valueOf(gameId));
            return null;
        }
    }

    public GameData rejoinGame(ChessGame.TeamColor playerColor, Integer gameId, String userUsername) {
        
        GameData game = getGame(gameId);
        if (game == null) { return null; }

        if (game.whiteUsername() != null && game.whiteUsername().equals(userUsername) || game.blackUsername() != null && game.blackUsername().equals(userUsername)) {
            return game;
        }

        return null;
    }

    public ChessGame.TeamColor stringToPlayerColor(String playerColorString) {
        String cleanString = playerColorString.trim().toLowerCase();
        return switch (cleanString) {
            case "white" -> ChessGame.TeamColor.WHITE;
            default -> ChessGame.TeamColor.BLACK;
        };
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
