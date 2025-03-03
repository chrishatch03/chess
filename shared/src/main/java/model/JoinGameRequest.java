package model;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor teamColor, Integer gameId){
}
