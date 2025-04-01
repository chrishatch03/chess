package ui;

import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.HashMap;

import chess.ChessGame;
import exception.*;
import model.*;

public class PostLoginUI {
    private final ServerFacade server;
    private final Repl repl;
    private HashMap<Integer, GameData> games = new HashMap<>();

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
                case "quit" -> quit();
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
            this.games = new HashMap<>();
            var games = server.listGames(repl.getAuthToken()).games();
            StringBuilder output = new StringBuilder(SET_TEXT_COLOR_WHITE + "Games Available: \n" + SET_TEXT_COLOR_BLACK + "to join a game enter command \">>> join <white/black/observer> <game number>\" \n" + RESET_TEXT_COLOR);
            int gameNum = 1;
            for (GameData currentGame : games) {
                this.games.put(gameNum, currentGame);
                output.append("\nGame ").append(String.valueOf(gameNum)).append(": ").append(currentGame.gameName());
                String white = (currentGame.whiteUsername() == null || currentGame.whiteUsername().isEmpty()) ? "available" : currentGame.whiteUsername();
                String black = (currentGame.blackUsername() == null || currentGame.blackUsername().isEmpty()) ? "available" : currentGame.blackUsername();
                output.append("    white: ").append(white).append("  black: ").append(black);
                gameNum++;
            }
            return output.toString();
        }
        throw new ResponseException(400, "Expected no params for 'list'");
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            var playerColor = params[0];
            // var games = server.listGames(repl.getAuthToken()).games();
            Integer gameNum = Integer.valueOf(params[1]);
            
            if (gameNum == null || !this.games.containsKey(gameNum)) { return "Could not find game " + params[1]; }

            var isObserver = (playerColor.toLowerCase().equals("observer")) ? true : false;
            if (isObserver == true) {
                GameData game = getGame(gameNum);
                this.repl.setCurrentGame(game);
                this.repl.setPlayerColor("white");
                this.repl.setObserver(true);
            }
            
            var truePlayerColor = stringToPlayerColor(playerColor);
            if (truePlayerColor == null) { 
                throw new ResponseException(400, playerColor + " invalid team color, only options are 'white' and 'black'"); 
            }

            GameData alreadyJoinedExistingGame = rejoinGame(truePlayerColor, this.games.get(gameNum).gameID(), this.repl.getUsername());
            if (alreadyJoinedExistingGame != null) {
                this.repl.setCurrentGame(alreadyJoinedExistingGame);
                this.repl.setPlayerColor(playerColor);
            }

            server.joinGame(new JoinGameRequest(playerColor, this.games.get(gameNum).gameID()), repl.getAuthToken()).toString();
            GameData newGame = getGame(this.games.get(gameNum).gameID());
            this.repl.setCurrentGame(newGame);
            this.repl.setPlayerColor(playerColor);
            return "";
        }
        throw new ResponseException(400, "Expected: <white/black/observer> <gameId>");
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

    public String quit() {
        return "quit";
    }

    public static String help() {
        return SET_TEXT_COLOR_BLUE + """
                - list
                - create <gameName>
                - join <white/black/observer> <gameId>
                - help
                - logout
                - clear
                - quit
                """;
    }

}
