package chess.piece.rules;

import chess.ChessBoard;
import chess.ChessMove;

public class RookRulesValidator extends PieceRulesValidator {
    @Override
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        var pathClear = false;

        int[] direction = getDirection(move);
        int yChange = direction[0];
        int xChange = direction[1];

        if (yChange > 0 && xChange == 0) { pathClear = isPathClear(board, move, 1, 0); }
        else if (yChange == 0 && xChange > 0) { pathClear = isPathClear(board, move, 0, 1); }
        else if (yChange < 0 && xChange == 0) { pathClear = isPathClear(board, move, -1, 0); }
        else if (yChange == 0 && xChange < 0) { pathClear = isPathClear(board, move, 0, -1); }

        return pathClear;
    }
}
