package chess.PawnRules;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public abstract class PieceRulesValidator {
    public abstract boolean isValidMove(ChessBoard board, ChessMove move);

    public boolean isPathClear(ChessBoard board, ChessMove move, int yInc, int xInc) {
        int row = move.getStartPosition().getRow();
        int col = move.getStartPosition().getColumn();

        ChessPosition evalPos = new ChessPosition(row + yInc, col + xInc);
        while (ChessBoard.inBounds(evalPos) && !evalPos.equals(move.getEndPosition())) {
            ChessPiece evalPiece = board.getPiece(evalPos);
            if (evalPiece != null) {
                return false;
            }
            evalPos = new ChessPosition(evalPos.getRow() + yInc, evalPos.getColumn() + xInc);
        }
        return true;
    }

    public int[] getDirection(ChessMove move) {
        int yChange = 0;
        if (move.getStartPosition().getRow() > move.getEndPosition().getRow()) {
            yChange = -1;
        } else if (move.getStartPosition().getRow() < move.getEndPosition().getRow()) {
            yChange = 1;
        }

        int xChange = 0;
        if (move.getStartPosition().getColumn() > move.getEndPosition().getColumn()) {
            xChange = -1;
        } else if (move.getStartPosition().getColumn() < move.getEndPosition().getColumn()) {
            xChange = 1;
        }

        return new int[] {yChange, xChange};
    }

    public boolean isTooFar(ChessMove move, int yLimitUp, int yLimitDown, int xLimitUp, int xLimitDown) {
        int startY = move.getStartPosition().getRow();
        int startX = move.getStartPosition().getColumn();
        int endY = move.getEndPosition().getRow();
        int endX = move.getEndPosition().getColumn();
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
