package chess;

public class KnightRulesValidator extends PieceRulesValidator {
    @Override
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        ChessPiece knight = board.getPiece(move.startPosition);

        if (knight == null) { return false; }

        if (isTooFar(move,2,2,2,2)) {
            return false;
        }

        return true;
    }
}
