package chess.PieceMoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {

    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    public static void getPathMoves(ChessBoard board, ChessPosition startPosition, int yInc, int xInc,
                                    ChessGame.TeamColor piececolor, ArrayList<ChessMove> validMoves) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        ChessPosition evalPos = new ChessPosition(row + yInc, col + xInc);
        while (ChessBoard.inBounds(evalPos)) {
            ChessPiece evalPiece = board.getPiece(evalPos);
            if (evalPiece == null) {
                validMoves.add(new ChessMove(startPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + yInc, evalPos.getColumn() + xInc);
            } else if (evalPiece.getTeamColor() != piececolor) {
                validMoves.add(new ChessMove(startPosition, evalPos, null));
                break;
            } else {
                break;
            }
        }
    }

    void getKMoves(ChessBoard board, ChessPosition startPosition, ChessPosition[] evalPositions,
                   ChessGame.TeamColor piececolor, ArrayList<ChessMove> validMoves) {

        for (ChessPosition evalPos: evalPositions) {
            if (ChessBoard.inBounds(evalPos)) {
                ChessPiece evalPiece = board.getPiece(evalPos);
                if (evalPiece == null) {
                    validMoves.add(new ChessMove(startPosition, evalPos, null));
                } else if (evalPiece.getTeamColor() != piececolor) {
                    validMoves.add(new ChessMove(startPosition, evalPos, null));
                }
            }
        }
    }


}
