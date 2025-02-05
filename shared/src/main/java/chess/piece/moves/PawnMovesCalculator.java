package chess.piece.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor pawnColor = board.getPiece(position).getTeamColor();

        if (pawnColor == ChessGame.TeamColor.WHITE) {
            evalPositions(board, position, 1, 2, 8, pawnColor, validMoves);
        } else if (pawnColor == ChessGame.TeamColor.BLACK) {
            evalPositions(board, position, -1, 7, 1, pawnColor, validMoves);
        }

        return validMoves;
    }

    void evalPositions(ChessBoard board, ChessPosition position, int yInc, int startRow, int promotionRow,
                       ChessGame.TeamColor pawnColor, ArrayList<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition moveOne = new ChessPosition(row + yInc, col);
        if (!ChessBoard.inBounds(moveOne)) {
            return;
        }

        ChessPiece moveOnePiece = board.getPiece(moveOne);
        if (moveOnePiece == null) {
            getMoves(position, moveOne, promotionRow, validMoves);

            if (row == startRow) {
                ChessPosition moveTwo = new ChessPosition(moveOne.getRow() + yInc, col);
                if (ChessBoard.inBounds(moveTwo)) {
                    ChessPiece moveTwoPiece = board.getPiece(moveTwo);
                    if (moveTwoPiece == null) {
                        getMoves(position, moveTwo, promotionRow, validMoves);

                    }
                }
            }
        }

        ChessPosition moveRight = new ChessPosition(row + yInc, col + 1);
        if (ChessBoard.inBounds(moveRight)) {
            ChessPiece moveRightPiece = board.getPiece(moveRight);
            if (moveRightPiece != null && board.getPiece(moveRight).getTeamColor() != pawnColor) {
                getMoves(position, moveRight, promotionRow, validMoves);

            }
        }

        ChessPosition moveLeft = new ChessPosition(row + yInc, col - 1);
        if (ChessBoard.inBounds(moveLeft)) {
            ChessPiece moveLeftPiece = board.getPiece(moveLeft);
            if (moveLeftPiece != null && board.getPiece(moveLeft).getTeamColor() != pawnColor) {
                getMoves(position, moveLeft, promotionRow, validMoves);

            }
        }
    }

    void getMoves(ChessPosition startPosition, ChessPosition endPosition, int promotionRow, ArrayList<ChessMove> validMoves) {
        if (endPosition.getRow() == promotionRow) {
            validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
            validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
        } else {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

}
