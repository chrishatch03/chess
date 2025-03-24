package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, AuthData.class, null);
    }

    public AuthData login(LoginRequest LoginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, LoginRequest, AuthData.class, null);
    }

    public Object logout(String authToken) throws ResponseException {
        var path = "/session";
        if (authToken.isEmpty()) { throw new ResponseException(400, "Invalid logout request: authToken was null"); }
        return this.makeRequest("DELETE", path, new EmptyRequest(), EmptyResponse.class, authToken);
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest, String authToken) throws ResponseException {
        var path = "/game";
        if (authToken.isEmpty()) { throw new ResponseException(400, "Invalid createGame request: authToken was null"); }
        return this.makeRequest("POST", path, createGameRequest, CreateGameResponse.class, authToken);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException {
        var path = "/game";
        if (authToken.isEmpty()) { throw new ResponseException(400, "Invalid listGames request: authToken was null"); }
        return this.makeRequest("GET", path, new EmptyRequest(), ListGamesResponse.class, authToken);
    }

    public Object joinGame(JoinGameRequest joinGameRequest, String authToken) throws ResponseException {
        var path = "/game";
        if (authToken.isEmpty()) { throw new ResponseException(400, "Invalid joinGame request: authToken was null"); }
        return this.makeRequest("PUT", path, joinGameRequest, EmptyResponse.class, authToken);
    }
    
    public EmptyResponse clearApp(EmptyRequest emptyRequest) throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, emptyRequest, EmptyResponse.class, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            

            if (authToken != null && !authToken.isEmpty()) {
                writeHeader(authToken, http);
            }

            if (!method.equals("GET") && request != null) {
                writeBody(request, http);
            }

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http) {
        http.addRequestProperty("Authorization", authToken);
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
