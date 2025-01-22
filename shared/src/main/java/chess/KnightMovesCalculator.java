package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor knightColor = board.getPiece(position).getTeamColor();

        ChessPosition[] knightPositions = {
                new ChessPosition(row + 2, col + 1),
                new ChessPosition(row + 1, col + 2),
                new ChessPosition(row - 1, col + 2),
                new ChessPosition(row - 2, col + 1),
                new ChessPosition(row + 2, col - 1),
                new ChessPosition(row + 1, col - 2),
                new ChessPosition(row - 1, col - 2),
                new ChessPosition(row - 2, col - 1),

        };

        validMoves.addAll(getKMoves(position, knightPositions, board, knightColor));

        return validMoves;
    }
}
