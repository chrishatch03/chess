
import ui.Repl;
import exception.*;
public class Main {
    public static void main(String[] args) throws ResponseException{
        System.out.println("♕ 240 Chess Client: ");

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new Repl(serverUrl).run();
    }
}