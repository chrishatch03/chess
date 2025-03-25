package ui;

import java.util.Arrays;
import exception.*;
import server.ServerFacade;
import model.*;
import static ui.EscapeSequences.*;

public class PreLoginUI {
    private final ServerFacade server;
    private final Repl repl;

    public PreLoginUI(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            AuthData successful = server.register(new RegisterRequest(username, password, email));
            this.repl.setAuthToken(successful.authToken());
            this.repl.setUsername(successful.username());
            return "Registered user " + successful.username();
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            AuthData authData = server.login(new LoginRequest(username, password));
            if (authData.username().isEmpty() || authData.authToken().isEmpty()) {
                throw new ResponseException(400, "Login response invalid in client");
            }
            repl.setAuthToken(authData.authToken());
            repl.setUsername(authData.username());
            return "Logged in user " + authData.username();
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                - register <username> <password> <email>
                - login <username> <password>
                - help
                - quit
                """;
    }

}
