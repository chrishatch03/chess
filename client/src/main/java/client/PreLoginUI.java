package client;

import java.util.Arrays;
import exception.*;
import server.ServerFacade;
import model.*;

public class PreLoginUI {
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;

    public PreLoginUI(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
                case "quit" -> "quit";
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
            System.out.println(params);
            return server.register(new RegisterRequest(username, password, email)).toString();
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            System.out.println("Logging in: username=" + username + " password=" + password);
            AuthData authData = server.login(new LoginRequest(username, password));
            if (authData.username().isEmpty() || authData.authToken().isEmpty()) {
                throw new ResponseException(400, "Login response invalid in client");
            }
            repl.setAuthToken(authData.authToken());
            repl.setUsername(authData.username());
            return authData.toString();
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    // public String signIn(String... params) throws ResponseException {
    //     if (params.length >= 1) {
    //         state = State.SIGNEDIN;
    //         visitorName = String.join("-", params);
    //         ws = new WebSocketFacade(serverUrl, notificationHandler);
    //         ws.enterPetShop(visitorName);
    //         return String.format("You signed in as %s.", visitorName);
    //     }
    //     throw new ResponseException(400, "Expected: <yourname>");
    // }

    // public String logout() throws ResponseException {
    //     assertSignedIn();
    //     ws.leavePetShop(userName);
    //     ws = null;
    //     state = State.SIGNEDOUT;
    //     return String.format("%s left the shop", userName);
    // }

    public String help() {
        return """
                - register <username> <password> <email>
                - login <username> <password>
                - help
                - quit
                """;
    }

}
