package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessGame.TeamColor kingColor = board.getPiece(position).getTeamColor();

        ChessPosition[] kingPositions = {
                new ChessPosition(row + 1, col),
                new ChessPosition(row + 1, col + 1),
                new ChessPosition(row, col + 1),
                new ChessPosition(row - 1, col + 1),
                new ChessPosition(row - 1, col),
                new ChessPosition(row - 1, col - 1),
                new ChessPosition(row, col - 1),
                new ChessPosition(row + 1, col - 1),
        };

        validMoves.addAll(getKMoves(position, kingPositions, board, kingColor));

        return validMoves;
    }
}
