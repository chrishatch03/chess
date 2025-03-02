import chess.*;
import server.Server;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserMemoryDAO;
import dataaccess.GameMemoryDAO;
import service.AuthService;
import service.UserService;
import service.GameService;

public class Main {
    public static void main(String[] args) {
        try {
            int port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

//            UserMemoryDAO userDAO = new UserMemoryDAO();
//            if (args.length >= 2 && args[1].equals("sql")) {
//                userDAO = new MySqlUserMemoryDAO();
//            }
//
//            AuthMemoryDAO authDAO = new AuthMemoryDAO();
//            if (args.length >= 2 && args[1].equals("sql")) {
//                authDAO = new MySqlAuthMemoryDAO();
//            }
//
//            GameMemoryDAO gameDAO = new GameMemoryDAO();
//            if (args.length >= 2 && args[1].equals("sql")) {
//                gameDAO = new MySqlGameMemoryDAO();
//            }

//            var userService = new UserService(userDAO);
//            var authService = new AuthService(authDAO);
//            var gameService = new GameService(gameDAO);
//            var server = new Server(authService, userService, gameService);

            var server = new Server().run(port);

//            port = server.port();
//            System.out.printf("Server started on port %d with %s%n", port, server.userDAO.getClass());
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        System.out.println("""
                Pet Server:
                java ServerMain <port> [sql]
                """);
    }
}