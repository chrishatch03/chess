package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public static void viewMove(ChessPosition position) {
        System.out.println(position.toString());
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition queenPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessGame.TeamColor queenColor = board.getPiece(queenPosition).getTeamColor();

//        Up
        ChessPosition evalPos = new ChessPosition(queenPosition.getRow() + 1, queenPosition.getColumn());
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn());
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn());
                break;
            } else {
                break;
            }
        }

//        UpRight
        evalPos = new ChessPosition(queenPosition.getRow() + 1, queenPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn() + 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn() + 1);
                break;
            } else {
                break;
            }
        }

//        Right
        evalPos = new ChessPosition(queenPosition.getRow(), queenPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() + 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() + 1);
                break;
            } else {
                break;
            }
        }

//        DownRight
        evalPos = new ChessPosition(queenPosition.getRow() - 1, queenPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn() + 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn() + 1);
                break;
            } else {
                break;
            }
        }

//        Down
        evalPos = new ChessPosition(queenPosition.getRow() - 1, queenPosition.getColumn());
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn());
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn());
                break;
            } else {
                break;
            }
        }

//        DownLeft
        evalPos = new ChessPosition(queenPosition.getRow() - 1, queenPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn() - 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() - 1, evalPos.getColumn() - 1);
                break;
            } else {
                break;
            }
        }

//        Left
        evalPos = new ChessPosition(queenPosition.getRow(), queenPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() - 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow(), evalPos.getColumn() - 1);
                break;
            } else {
                break;
            }
        }

//        UpLeft
        evalPos = new ChessPosition(queenPosition.getRow() + 1, queenPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn() - 1);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                QueenMovesCalculator.viewMove(evalPos);
                validMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + 1, evalPos.getColumn() - 1);
                break;
            } else {
                break;
            }
        }

        return validMoves;
    }
}
