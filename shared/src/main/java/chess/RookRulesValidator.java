package chess;

public class RookRulesValidator extends PieceRulesValidator {
    @Override
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        boolean pathClear = false;

        int yChange = 0;
        if (move.startPosition.getRow() > move.endPosition.getRow()) {
            yChange = -1;
        } else if (move.startPosition.getRow() < move.endPosition.getRow()) {
            yChange = +1;
        }

        int xChange = 0;
        if (move.startPosition.getColumn() > move.endPosition.getColumn()) {
            xChange = -1;
        } else if (move.startPosition.getColumn() < move.endPosition.getColumn()) {
            xChange = +1;
        }

        if (yChange > 0 && xChange == 0) { pathClear = isPathClear(board, move, 1, 0); }
        else if (yChange > 0 && xChange > 0) { pathClear = false; }
        else if (yChange == 0 && xChange > 0) { pathClear = isPathClear(board, move, 0, 1); }
        else if (yChange < 0 && xChange > 0) { pathClear = false; }
        else if (yChange < 0 && xChange == 0) { pathClear = isPathClear(board, move, -1, 0); }
        else if (yChange < 0 && xChange < 0) { pathClear = false; }
        else if (yChange == 0 && xChange < 0) { pathClear = isPathClear(board, move, 0, -1); }
        else if (yChange > 0 && xChange < 0) { pathClear = false; }

        return pathClear;
    }
}
