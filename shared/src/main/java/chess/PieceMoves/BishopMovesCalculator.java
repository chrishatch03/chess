package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor bishopColor = board.getPiece(position).getTeamColor();

        getPathMoves(board, position, 1, 1, bishopColor, validMoves);
        getPathMoves(board, position, -1, 1, bishopColor, validMoves);
        getPathMoves(board, position, -1, -1, bishopColor, validMoves);
        getPathMoves(board, position, 1, -1, bishopColor, validMoves);

        return validMoves;
    }

}
