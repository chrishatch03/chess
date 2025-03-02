package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import java.util.Map;

public class Server {

    private final UserService userService;
    private final AuthService authService;
    private final GameService gameService;
//    private final WebSocketHandler webSocketHandler;

    public Server(AuthService authService, UserService userService, GameService gameService) {
        this.authService = authService;
        this.userService = userService;
        this.gameService = gameService;
//        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.webSocket("/ws", webSocketHandler);

//        REGISTER USER
        Spark.post("/user", this::registerUser);
//        LOGIN
//        Spark.post("/session", this::login);

//        Spark.post("/authData", this::addAuthData);
//        Spark.post("/gameData", this::addGameData);
//        Spark.get("/userData", this::getUserData);
//        Spark.get("/authData", this::getAuthData);
//        Spark.get("/gameData", this::getGameData);
//        Spark.delete("/userData/:id", this::deleteUserData);
//        Spark.delete("/userData", this::deleteAllUserData);
//        Spark.delete("/authData/:id", this::deleteAuthData);
//        Spark.delete("/authData", this::deleteAllAuthData);
//        Spark.delete("/gameData/:id", this::deleteGameData);
//        Spark.delete("/gameData", this::deleteAllGameData);
//        Spark.delete("/db", this::deleteAppData);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.StatusCode());
        res.body(ex.toJson());
    }

//    POST REQUESTS
    private Object registerUser(Request req, Response res) throws ResponseException {
        var userData = new Gson().fromJson(req.body(), UserData.class);
        userData = userService.registerUser(userData);
//        webSocketHandler.makeNoise(userData.name(), userData.sound());
        return new Gson().toJson(userData);
    }

//    private Object login(Request req, Response res) throws ResponseException {
//        var authCredentials = new Gson().fromJson(req.body(), AuthData.class);
//        authCredentials = service.login(authCredentials);
//        return new Gson().toJson(authCredentials);
//    }
//    private Object addAuthData(Request req, Response res) throws ResponseException {
//        var authData = new Gson().fromJson(req.body(), AuthData.class);
//        authData = service.addAuthData(authData);
////        webSocketHandler.makeNoise(authData.name(), authData.sound());
//        return new Gson().toJson(authData);
//    }
//    private Object addGameData(Request req, Response res) throws ResponseException {
//        var gameData = new Gson().fromJson(req.body(), GameData.class);
//        gameData = service.addGameData(gameData);
////        webSocketHandler.makeNoise(gameData.name(), gameData.sound());
//        return new Gson().toJson(gameData);
//    }
//
////  GET REQUESTS
//    private Object getUserData(Request req, Response res) throws ResponseException {
//        res.type("application/json");
//        var userData = service.getUserData().toArray();
//        return new Gson().toJson(Map.of("userData", userData));
//    }
//    private Object getAuthData(Request req, Response res) throws ResponseException {
//        res.type("application/json");
//        var authData = service.getAuthData().toArray();
//        return new Gson().toJson(Map.of("authData", authData));
//    }
//    private Object getGameData(Request req, Response res) throws ResponseException {
//        res.type("application/json");
//        var gameData = service.getGameData().toArray();
//        return new Gson().toJson(Map.of("gameData", gameData));
//    }
//
////  DELETE REQUESTS
//    private Object deleteUserData(Request req, Response res) throws ResponseException {
//        var id = Integer.parseInt(req.params(":id"));
//        var userData = service.getUserData(id);
//        if (userData != null) {
//            service.deleteUserData(id);
//            webSocketHandler.makeNoise(userData.name(), userData.sound());
//            res.status(200);
//        } else {
//            res.status(500);
//        }
//        return "";
//    }
//    private Object deleteAuthData(Request req, Response res) throws ResponseException {
//        var id = Integer.parseInt(req.params(":id"));
//        var authData = service.getAuthData(id);
//        if (authData != null) {
//            service.deleteAuthData(id);
//            webSocketHandler.makeNoise(authData.name(), authData.sound());
//            res.status(200);
//        } else {
//            res.status(500);
//        }
//        return "";
//    }
//    private Object deleteGameData(Request req, Response res) throws ResponseException {
//        var id = Integer.parseInt(req.params(":id"));
//        var gameData = service.getGameData(id);
//        if (gameData != null) {
//            service.deleteGameData(id);
//            webSocketHandler.makeNoise(gameData.name(), gameData.sound());
//            res.status(200);
//        } else {
//            res.status(500);
//        }
//        return "";
//    }
//
////  DELETE ALL
//    private Object deleteAllUserData(Request req, Response res) throws ResponseException {
//        service.deleteAllUserData();
//        res.status(200);
//        return "";
//    }
//    private Object deleteAllAuthData(Request req, Response res) throws ResponseException {
//        service.deleteAllAuthData();
//        res.status(200);
//        return "";
//    }
//    private Object deleteAllGameData(Request req, Response res) throws ResponseException {
//        service.deleteAllGameData();
//        res.status(200);
//        return "";
//    }
////    Clear Entire Application
//    private Object deleteAppData(Request req, Response res) throws ResponseException {
//        service.deleteAllUserData();
//        service.deleteAllAuthData();
//        service.deleteAllGameData();
//        res.status(200);
//        return "";
//    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
