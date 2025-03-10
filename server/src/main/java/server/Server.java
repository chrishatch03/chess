package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import dataaccess.AuthMemoryDAO;
import dataaccess.UserMemoryDAO;
import dataaccess.GameMemoryDAO;
import spark.*;

public class Server {

    private final UserService userService = new UserService(new UserMemoryDAO());
    private final AuthService authService = new AuthService(new AuthMemoryDAO());
    private final GameService gameService = new GameService(new GameMemoryDAO());

    private final Gson serializer = new Gson();

    public Server() {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        res.type("application/json");
        res.body(ex.toJson());
    }

    private Object sendResponse(Request req, Response res, Object body) {
        res.status(200);
        res.type("application/json");
        return serializer.toJson(body);
    }

//    POST REQUESTS
    private Object register(Request req, Response res) throws ResponseException {
        var userData = serializer.fromJson(req.body(), UserData.class);
        if (userData.username() == null || userData.username().isBlank() ||
                userData.password() == null || userData.password().isBlank() ||
                userData.email() == null || userData.email().isBlank()) {
            throw new ResponseException(400, "Error: bad request");
        }
        userData = userService.register(userData);
        var authData = authService.add(userData.username());
        return sendResponse(req,res,authData);
    }

//  LOGIN
    private Object login(Request req, Response res) throws ResponseException {
        var authCredentials = serializer.fromJson(req.body(), LoginRequest.class);
        UserData userData = userService.verifyCredentials(authCredentials);
        AuthData authData = authService.add(userData.username());
        return sendResponse(req,res,authData);
    }

    //  LOGOUT
    private Object logout(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isBlank()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        try {
            authService.sessionExists(authToken);
            authService.delete(authToken);
            return sendResponse(req, res, new EmptyResponse());
        } catch (ResponseException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

//    CREATE GAME
    private Object createGame(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isBlank()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        try {
            authService.sessionExists(authToken);
            var createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
            String gameName = createGameRequest.gameName();
            if (gameName == null || gameName.isBlank()) {
                throw new ResponseException(400, "Error: bad request");
            }
            GameData gameData = gameService.add(createGameRequest.gameName());
            return sendResponse(req,res,new CreateGameResponse(gameData.gameID()));
        } catch (Exception ex) {
            throw new ResponseException(401, ex.getMessage());
        }
    }

    //    LIST GAMES
    private Object listGames(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        authService.sessionExists(authToken);
        var games = gameService.listAll();
        return sendResponse(req,res,new ListGamesResponse(games));
    }

    //    JOIN GAME
    private Object joinGame(Request req, Response res) throws ResponseException {
            var authToken = req.headers("Authorization");
            if (authToken == null || authToken.isEmpty()) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            var authData = authService.sessionExists(authToken);
            var userData = userService.get(authData.username());
            var joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
            System.out.println("playerColor: " + joinGameRequest.playerColor());
            if (!"WHITE".equals(joinGameRequest.playerColor()) &&
                    !"BLACK".equals(joinGameRequest.playerColor())) {
                throw new ResponseException(400, "Error: bad request");
            }
            if (joinGameRequest.gameID() == null) {
                throw new ResponseException(400, "Error: bad request - invalid game ID");
            }
            gameService.get(joinGameRequest.gameID());
            gameService.joinGame(joinGameRequest, userData);
            return sendResponse(req,res,new EmptyResponse());
    }

//    Clear Entire Application
    private Object clearApp(Request req, Response res) throws ResponseException {
        userService.deleteAll();
        authService.deleteAll();
        gameService.deleteAll();
        return sendResponse(req,res,new EmptyResponse());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
