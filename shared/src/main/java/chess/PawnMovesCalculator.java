package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    ArrayList<ChessMove> getMoves(int lastRow, ChessPosition pawnPosition, ChessPosition position) {
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


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pawnPosition) {
        int y = pawnPosition.getRow();
        int x = pawnPosition.getColumn();

        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(pawnPosition);
        ChessGame.TeamColor pawnColor = pawn.getTeamColor();

        if (pawnColor == ChessGame.TeamColor.WHITE) {

            ChessPosition upOne = new ChessPosition(y + 1, x);
            if (ChessBoard.inBounds(upOne) && board.isEmpty(upOne)) {

                validMoves.addAll(getMoves(8, pawnPosition, upOne));

                if (y == 2) {
                    ChessPosition upTwo = new ChessPosition(y + 2, x);
                    if (ChessBoard.inBounds(upTwo) && board.isEmpty(upTwo)) {
                        validMoves.addAll(getMoves(8, pawnPosition, upTwo));
                    }
                }

            }

            ChessPosition upRight = new ChessPosition(y + 1, x + 1);
            if (ChessBoard.inBounds(upRight) && !board.isEmpty(upRight)) {
                if (board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.addAll(getMoves(8, pawnPosition, upRight));
                }
            }

            ChessPosition upLeft = new ChessPosition(y + 1, x - 1);
            if (ChessBoard.inBounds(upLeft) && !board.isEmpty(upLeft)) {
                if (board.getPiece(upLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    validMoves.addAll(getMoves(8, pawnPosition, upLeft));
                }
            }

        } else if (pawnColor == ChessGame.TeamColor.BLACK) {

            ChessPosition downOne = new ChessPosition(y - 1, x);
            if (ChessBoard.inBounds(downOne) && board.isEmpty(downOne)) {

                validMoves.addAll(getMoves(1, pawnPosition, downOne));

                if (y == 7) {
                    ChessPosition downTwo = new ChessPosition(y - 2, x);
                    if (ChessBoard.inBounds(downTwo) && board.isEmpty(downTwo)) {
                        validMoves.addAll(getMoves(1, pawnPosition, downTwo));
                    }
                }
            }

            ChessPosition downRight = new ChessPosition(y - 1, x + 1);
            if (ChessBoard.inBounds(downRight) && !board.isEmpty(downRight)) {
                if (board.getPiece(downRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.addAll(getMoves(1, pawnPosition, downRight));
                }
            }

            ChessPosition downLeft = new ChessPosition(y - 1, x - 1);
            if (ChessBoard.inBounds(downLeft) && !board.isEmpty(downLeft)) {
                if (board.getPiece(downLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    validMoves.addAll(getMoves(1, pawnPosition, downLeft));
                }
            }
        }

        return validMoves;
    }
}
