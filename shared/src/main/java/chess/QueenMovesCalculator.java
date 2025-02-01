package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor queenColor = board.getPiece(position).getTeamColor();

        getDMoves(board, position, 1, 0, queenColor, validMoves);
        getDMoves(board, position, 1, 1, queenColor, validMoves);
        getDMoves(board, position, 0, 1, queenColor, validMoves);
        getDMoves(board, position, -1, 1, queenColor, validMoves);
        getDMoves(board, position, -1, 0, queenColor, validMoves);
        getDMoves(board, position, -1, -1, queenColor, validMoves);
        getDMoves(board, position, 0, -1, queenColor, validMoves);
        getDMoves(board, position, 1, -1, queenColor, validMoves);

        return validMoves;
    }
}
