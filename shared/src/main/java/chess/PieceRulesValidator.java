package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceRulesValidator {
    public abstract boolean isValidMove(ChessBoard board, ChessMove move);

//    public boolean isPathClear(ChessBoard board, ChessMove move) {
//
//        int startY = move.startPosition.getRow();
//        int startX = move.startPosition.getColumn();
//        int endY = move.endPosition.getRow();
//        int endX = move.endPosition.getColumn();
//        int yInc;
//        int xInc;
//
//        if (startY < endY) {
//            yInc = +1;
//        } else if (startY > endY) {
//            yInc = -1;
//        } else {
//            yInc = 0;
//        }
//
//        if (startX < endX) {
//            xInc = +1;
//        } else if (startX > endX) {
//            xInc = -1;
//        } else {
//            xInc = 0;
//        }
//
//        for (int row = startY, col = startX; row != endY || col != endX; row += yInc, col += xInc) {
//            if (board.getPiece(new ChessPosition(row, col)) != null) {
//                return false;
//            }
//        }
//
//        return true;
//    }

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

//        validate y movement
        if (startY > endY) {
            if ( Math.abs(startY - endY) > yLimitDown) {
                tooFar = true;
            }
        } else {
            if ( Math.abs(endY - startY) > yLimitUp) {
                tooFar = true;
            }
        }

//        validate x movement
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
