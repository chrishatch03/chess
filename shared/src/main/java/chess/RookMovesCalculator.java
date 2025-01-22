package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition rookPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor rookColor = board.getPiece(rookPosition).getTeamColor();

        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 1, 0, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 0, 1, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, -1, 0, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 0, -1, board));

        return validMoves;
    }
}
