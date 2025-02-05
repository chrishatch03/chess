package chess.PawnRules;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;

public class PawnRulesValidator extends PieceRulesValidator {

    @Override
    public boolean isValidMove(ChessBoard board, ChessMove move) {
        ChessPiece pawn = board.getPiece(move.getStartPosition());
        if (pawn == null) {
            return false;
        }

        ChessGame.TeamColor pawnColor = pawn.getTeamColor();
        int startRow;
        if (pawnColor == ChessGame.TeamColor.WHITE) {
            startRow = 2;
        } else {
            startRow = 7;
        }

        int xChange = move.getStartPosition().getColumn() - move.getEndPosition().getColumn();
        if (Math.abs(xChange) > 1) { return false; }

        if (Math.abs(xChange) > 0) {
            ChessPiece capturePiece = board.getPiece(move.getEndPosition());
            if (capturePiece == null) { return false; }
        }

        int xLimitUp = 1;
        int xLimitDown = 1;
        if (xChange > 0) {
            xLimitUp = 0;
            xLimitDown = 1;
        } else if (xChange < 0) {
            xLimitUp = 1;
            xLimitDown = 0;
        } else {
            xLimitUp = 0;
            xLimitDown = 0;
        }

        int pawnStartY = move.getStartPosition().getRow();

        if (pawnColor == ChessGame.TeamColor.WHITE) {
            if (pawnStartY == startRow) {
                if (isTooFar(move, 2, 0, xLimitUp, xLimitDown)) {
                    return false;
                }

            } else {
                if(isTooFar(move, 1, 0, xLimitUp, xLimitDown)){
                    return false;
                }
            }

        } else {
            if (pawnStartY == startRow) {
                if (isTooFar(move, 0, 2, xLimitUp, xLimitDown)) {
                    return false;
                }

            } else {
                if(isTooFar(move, 0, 1, xLimitUp, xLimitDown)) {
                    return false;
                }
            }
        }

        return true;
    }
}
