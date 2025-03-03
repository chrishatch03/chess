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
        res.status(ex.StatusCode());
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
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        authService.delete(authToken);
        return sendResponse(req,res,new EmptyResponse());
    }

//    CREATE GAME
    private Object createGame(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        var createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
        try {
            GameData gameData = gameService.add(createGameRequest.gameName());
            return sendResponse(req,res,gameData);

        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //    LIST GAMES
    private Object listGames(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        var games = gameService.listAll();
        return sendResponse(req,res,games);
    }


    //    JOIN GAME
    private Object joinGame(Request req, Response res) throws ResponseException {
        var authToken = req.headers("Authorization");
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        var authData = authService.get(authToken);
        if (authData == null) { throw new ResponseException(401, "Error: unauthorized"); }

        var userData = userService.get(authData.username());
        if (userData == null) { throw new ResponseException(500, "Error: auth session exists but unable to get user"); }

        var joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        var gameData = gameService.joinGame(joinGameRequest, userData);
        return sendResponse(req,res,gameData);
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
