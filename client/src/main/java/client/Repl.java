package client;

// import client.websocket.NotificationHandler;
// import webSocketMessages.Notification;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final PreLoginUI preLoginClient;

    public Repl(String serverUrl) {
        preLoginClient = new PreLoginUI(serverUrl, this);
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = preLoginClient.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        scanner.close();
    }

    // public void notify(Notification notification) {
    //     System.out.println(RED + notification.message());
    //     printPrompt();
    // }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_BLACK + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
