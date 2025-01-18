package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition knightPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor knightColor = board.getPiece(knightPosition).getTeamColor();
        var y = knightPosition.getRow();
        var x = knightPosition.getColumn();

        ChessPosition[] possibleMoves = {
                new ChessPosition(y + 2, x + 1),
                new ChessPosition(y + 1, x + 2),
                new ChessPosition(y - 1, x + 2),
                new ChessPosition(y - 2, x + 1),
                new ChessPosition(y - 2, x - 1),
                new ChessPosition(y - 1, x - 2),
                new ChessPosition(y + 1, x - 2),
                new ChessPosition(y + 2, x - 1),
        };

        for (ChessPosition evalPos: possibleMoves) {
            if (!ChessBoard.inBounds(evalPos)) { continue; }
            if (board.isEmpty(evalPos)) {
                validMoves.add(new ChessMove(knightPosition, evalPos, null));
            } else if (knightColor != board.getPiece(evalPos).getTeamColor()) {
                validMoves.add(new ChessMove(knightPosition, evalPos, null));
            } else {
                continue;
            }
        }

        return validMoves;
    }
}
