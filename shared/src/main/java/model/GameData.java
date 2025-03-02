package model;
import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameId, String whiteUsername, String blackUsername, String gameName, ChessGame game ) {

    public GameData setWhiteUsername(String whiteUsername) {
        return new GameData(this.gameId, whiteUsername, this.blackUsername, this.gameName, this.game);
    }

    public GameData setBlackUsername(String blackUsername) {
        return new GameData(this.gameId, this.whiteUsername, blackUsername, this.gameName, this.game);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
