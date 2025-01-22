package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition bishopPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece bishop = board.getPiece(bishopPosition);
        ChessGame.TeamColor bishopColor = bishop.getTeamColor();

        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, 1, 1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, -1, 1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, -1, -1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, 1, -1, board));

        return validMoves;
    }

}
