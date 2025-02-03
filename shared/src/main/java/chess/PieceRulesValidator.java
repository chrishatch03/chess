package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceRulesValidator {
    public abstract boolean isValidMove(ChessBoard board, ChessMove moves);

    public boolean isPathClear(ChessBoard board, ChessMove move) {
        int startY = move.startPosition.getRow();
        int startX = move.startPosition.getColumn();
        int endY = move.endPosition.getRow();
        int endX = move.endPosition.getColumn();
        int yInc;
        int xInc;

        if (startY < endY) {
            yInc = +1;
        } else if (startY > endY) {
            yInc = -1;
        } else {
            yInc = 0;
        }

        if (startX < endX) {
            xInc = +1;
        } else if (startX > endX) {
            xInc = -1;
        } else {
            xInc = 0;
        }

        for (int row = startY, col = startX; row != endY || col != endX; row += yInc, col += xInc) {
            if (board.getPiece(new ChessPosition(row, col)) != null) {
                return false;
            }
        }

        return true;
    }
}
