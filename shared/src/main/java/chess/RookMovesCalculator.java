package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor rookColor = board.getPiece(position).getTeamColor();

        getPathMoves(board, position, 1, 0, rookColor, validMoves);
        getPathMoves(board, position, 0, 1, rookColor, validMoves);
        getPathMoves(board, position, -1, 0, rookColor, validMoves);
        getPathMoves(board, position, 0, -1, rookColor, validMoves);

        return validMoves;
    }

}
