package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    ArrayList<ChessMove> getPMoves(int lastRow, ChessPosition pawnPosition, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();

        if (position.getRow() == lastRow) {
            validMoves.add(new ChessMove(pawnPosition, position, ChessPiece.PieceType.QUEEN));
            validMoves.add(new ChessMove(pawnPosition, position, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(pawnPosition, position, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(pawnPosition, position, ChessPiece.PieceType.KNIGHT));
        } else {
            validMoves.add(new ChessMove(pawnPosition, position, null));
        }
        return validMoves;
    }

    ArrayList<ChessMove> evaluatePawnMoves(int yIncrement, int lastRow, int firstRow, ChessPosition pawnPosition, ChessBoard board, ChessGame.TeamColor opposingTeamColor) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int y = pawnPosition.getRow();
        int x = pawnPosition.getColumn();

        ChessPosition moveOne = new ChessPosition(y + yIncrement, x);
        if (ChessBoard.inBounds(moveOne) && board.isEmpty(moveOne)) {

            validMoves.addAll(getPMoves(lastRow, pawnPosition, moveOne));

            if (y == firstRow) {
                ChessPosition moveTwo = new ChessPosition(moveOne.getRow() + yIncrement, x);
                if (ChessBoard.inBounds(moveTwo) && board.isEmpty(moveTwo)) {
                    validMoves.addAll(getPMoves(lastRow, pawnPosition, moveTwo));
                }
            }

        }

        ChessPosition upRight = new ChessPosition(y + yIncrement, x + 1);
        if (ChessBoard.inBounds(upRight) && !board.isEmpty(upRight)) {
            if (board.getPiece(upRight).getTeamColor() == opposingTeamColor) {
                validMoves.addAll(getPMoves(lastRow, pawnPosition, upRight));
            }
        }

        ChessPosition upLeft = new ChessPosition(y + yIncrement, x - 1);
        if (ChessBoard.inBounds(upLeft) && !board.isEmpty(upLeft)) {
            if (board.getPiece(upLeft).getTeamColor() == opposingTeamColor) {
                validMoves.addAll(getPMoves(lastRow, pawnPosition, upLeft));
            }
        }

        return validMoves;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pawnPosition) {

        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(pawnPosition);
        ChessGame.TeamColor pawnColor = pawn.getTeamColor();

        if (pawnColor == ChessGame.TeamColor.WHITE) {
            validMoves.addAll(evaluatePawnMoves(1, 8, 2, pawnPosition, board, ChessGame.TeamColor.BLACK));
        } else if (pawnColor == ChessGame.TeamColor.BLACK) {
            validMoves.addAll(evaluatePawnMoves(-1, 1, 7, pawnPosition, board, ChessGame.TeamColor.WHITE));
        }

        return validMoves;
    }
}
