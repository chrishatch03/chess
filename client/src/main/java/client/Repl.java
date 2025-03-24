package client;

import java.util.Scanner;
import model.*;

import static ui.EscapeSequences.*;

public class Repl {
    private final PreLoginUI preLoginClient;
    private final PostLoginUI postLoginClient;
    private String authToken = "";
    private String username = "";
    private GameData currentGame = null;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginUI(serverUrl, this);
        postLoginClient = new PostLoginUI(serverUrl, this);

    }

    public void run() {
        System.out.println("Welcome to chess. Register or sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String userInput = scanner.nextLine();
            System.out.print(SET_TEXT_COLOR_BLACK);
            try {
                if (authToken.isEmpty()) {
                    result = preLoginClient.eval(userInput);
                } else {
                    if (currentGame == null) {
                        result = postLoginClient.eval(userInput);
                    } else {
                        result = postLoginClient.eval(userInput);
                    }
                }
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        scanner.close();
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String newAuthToken) {
        this.authToken = newAuthToken;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
