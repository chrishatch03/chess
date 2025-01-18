package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition rookPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor rookColor = board.getPiece(rookPosition).getTeamColor();

//        Up
        ChessPosition evalPos = new ChessPosition(rookPosition.getRow() + 1, rookPosition.getColumn());
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn());
            } else if (board.getPiece(evalPos).getTeamColor() != rookColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn());
                break;
            } else {
                break;
            }
        }

//        Right
        evalPos = new ChessPosition(rookPosition.getRow(), rookPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() + 1);
            } else if (board.getPiece(evalPos).getTeamColor() != rookColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() + 1);
                break;
            } else {
                break;
            }
        }

//        Down
        evalPos = new ChessPosition(rookPosition.getRow() - 1, rookPosition.getColumn());
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn());
            } else if (board.getPiece(evalPos).getTeamColor() != rookColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn());
                break;
            } else {
                break;
            }
        }

//        Left
        evalPos = new ChessPosition(rookPosition.getRow(), rookPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() - 1);
            } else if (board.getPiece(evalPos).getTeamColor() != rookColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() - 1);
                break;
            } else {
                break;
            }
        }

        return validMoves;
    }
}
