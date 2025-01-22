package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition queenPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor queenColor = board.getPiece(queenPosition).getTeamColor();

        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, 0, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 0, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, 0, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, -1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 0, -1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, -1, board));

        return validMoves;
    }



}
