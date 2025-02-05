package chess;

public class BishopRulesValidator extends PieceRulesValidator {
    @Override
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        boolean pathClear = false;

        int[] direction = getDirection(move);
        int yChange = direction[0];
        int xChange = direction[1];

        if (yChange > 0 && xChange > 0) { pathClear = isPathClear(board, move, 1, 1); }
        else if (yChange < 0 && xChange > 0) { pathClear = isPathClear(board, move, -1, 1); }
        else if (yChange < 0 && xChange < 0) { pathClear = isPathClear(board, move, -1, -1); }
        else if (yChange > 0 && xChange < 0) { pathClear = isPathClear(board, move, 1, -1); }

        return pathClear;
    }
}
