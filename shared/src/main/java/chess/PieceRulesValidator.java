package chess;

public abstract class PieceRulesValidator {
    public abstract boolean isValidMove(ChessBoard board, ChessMove move);

    public boolean isPathClear(ChessBoard board, ChessMove move, int yInc, int xInc) {
        int row = move.startPosition.getRow();
        int col = move.startPosition.getColumn();

        ChessPosition evalPos = new ChessPosition(row + yInc, col + xInc);
        while (ChessBoard.inBounds(evalPos) && !evalPos.equals(move.endPosition)) {
            ChessPiece evalPiece = board.getPiece(evalPos);
            if (evalPiece != null) {
                return false;
            }
            evalPos = new ChessPosition(evalPos.getRow() + yInc, evalPos.getColumn() + xInc);
        }
        return true;
    }

    public boolean isTooFar(ChessMove move, int yLimitUp, int yLimitDown, int xLimitUp, int xLimitDown) {
        int startY = move.startPosition.getRow();
        int startX = move.startPosition.getColumn();
        int endY = move.endPosition.getRow();
        int endX = move.endPosition.getColumn();
        boolean tooFar = false;

        if (startY > endY) {
            if ( Math.abs(startY - endY) > yLimitDown) {
                tooFar = true;
            }
        } else {
            if ( Math.abs(endY - startY) > yLimitUp) {
                tooFar = true;
            }
        }

        if (startX > endX) {
            if ( Math.abs(startX - endX) > xLimitDown) {
                tooFar = true;
            }
        } else {
            if ( Math.abs(endX - startX) > xLimitUp) {
                tooFar = true;
            }
        }

        return tooFar;
    }
}
