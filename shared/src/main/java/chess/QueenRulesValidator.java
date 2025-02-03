package chess;

import java.util.ArrayList;

public class QueenRulesValidator extends PieceRulesValidator {

    public boolean isValidMove(ChessBoard board, ChessMove move) {

        int startY = move.startPosition.getRow();
        int startX = move.startPosition.getColumn();
        int endY = move.endPosition.getRow();
        int endX = move.endPosition.getColumn();
        boolean pathClear = false;

        int yChange = 0;
        if (startY > endY) {
            yChange = -1;
        } else if (startY < endY) {
            yChange = +1;
        }

        int xChange = 0;
        if (startX > endX) {
            xChange = -1;
        } else if (startX < endX) {
            xChange = +1;
        }

        if (yChange > 0 && xChange == 0) { pathClear = isPathClear(board, move, 1, 0); }
        else if (yChange > 0 && xChange > 0) { pathClear = isPathClear(board, move, 1, 1); }
        else if (yChange == 0 && xChange > 0) { pathClear = isPathClear(board, move, 0, 1); }
        else if (yChange < 0 && xChange > 0) { pathClear = isPathClear(board, move, -1, 1); }
        else if (yChange < 0 && xChange == 0) { pathClear = isPathClear(board, move, -1, 0); }
        else if (yChange < 0 && xChange < 0) { pathClear = isPathClear(board, move, -1, -1); }
        else if (yChange == 0 && xChange < 0) { pathClear = isPathClear(board, move, 0, -1); }
        else if (yChange > 0 && xChange < 0) { pathClear = isPathClear(board, move, 1, -1); }

        return pathClear;
    }

}
