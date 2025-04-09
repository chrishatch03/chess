package model;

import chess.ChessGame;

public record UpdateGameRequest(String playerColor, Integer gameID, GameData game) {
    
}
