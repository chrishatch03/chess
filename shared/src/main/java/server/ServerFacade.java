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
        var path = "/register";
        return this.makeRequest("POST", path, registerRequest, AuthData.class);
    }

    public AuthData login(LoginRequest LoginRequest) throws ResponseException {
        var path = "/login";
        return this.makeRequest("POST", path, LoginRequest, AuthData.class);
    }

    public Object logout(EmptyRequest emptyRequest) throws ResponseException {
        var path = "/logout";
        return this.makeRequest("DELETE", path, emptyRequest, EmptyResponse.class);
    }

    public Object createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, createGameRequest, CreateGameResponse.class);
    }

    public Object listGames(EmptyRequest emptyRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, emptyRequest, ListGamesResponse.class);
    }

    public Object joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("PUT", path, joinGameRequest, EmptyResponse.class);
    }
    
    public EmptyResponse clearApp(EmptyRequest emptyRequest) throws ResponseException {
        var path = "/db";
        return this.makeRequest("DELETE", path, emptyRequest, EmptyResponse.class);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
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
