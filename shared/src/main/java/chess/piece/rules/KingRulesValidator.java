package chess.piece.rules;

import chess.ChessBoard;
import chess.ChessMove;

public class KingRulesValidator extends PieceRulesValidator {
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        if (isTooFar(move, 1,1,1,1)) {
            return false;
        }

        return true;
    }

}
