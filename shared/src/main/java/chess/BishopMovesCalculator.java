package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition bishopPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessPiece bishop = chessBoard.getPiece(bishopPosition);

//      get moves going up and to the right
        ChessPosition evalPosition = new ChessPosition(bishopPosition.getRow() + 1, bishopPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPosition)) {

            if (chessBoard.isEmpty(evalPosition)) {
                ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                validMoves.add(newMove);
            } else {
                ChessPiece evalPiece = chessBoard.getPiece(evalPosition);
                ChessGame.TeamColor evalTeamColor = evalPiece.getTeamColor();

                if (bishop.getTeamColor() == evalTeamColor) {
                    break;
                } else if (bishop.getTeamColor() != evalTeamColor) {
                    ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                    validMoves.add(newMove);
                    break;
                }
            }
            evalPosition = new ChessPosition(evalPosition.getRow() + 1, evalPosition.getColumn() + 1);
        }

//      Down and to the right
        evalPosition = new ChessPosition(bishopPosition.getRow() - 1, bishopPosition.getColumn() + 1);
        while (ChessBoard.inBounds(evalPosition)) {

            if (chessBoard.isEmpty(evalPosition)) {
                ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                validMoves.add(newMove);
            } else {
                ChessPiece evalPiece = chessBoard.getPiece(evalPosition);
                ChessGame.TeamColor evalTeamColor = evalPiece.getTeamColor();

                if (bishop.getTeamColor() == evalTeamColor) {
                    break;
                } else if (bishop.getTeamColor() != evalTeamColor) {
                    ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                    validMoves.add(newMove);
                    break;
                }
            }
            evalPosition = new ChessPosition(evalPosition.getRow() - 1, evalPosition.getColumn() + 1);
        }

//      Down and to the left
        evalPosition = new ChessPosition(bishopPosition.getRow() - 1, bishopPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPosition)) {

            if (chessBoard.isEmpty(evalPosition)) {
                ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                validMoves.add(newMove);
            } else {
                ChessPiece evalPiece = chessBoard.getPiece(evalPosition);
                ChessGame.TeamColor evalTeamColor = evalPiece.getTeamColor();

                if (bishop.getTeamColor() == evalTeamColor) {
                    break;
                } else if (bishop.getTeamColor() != evalTeamColor) {
                    ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                    validMoves.add(newMove);
                    break;
                }
            }
            evalPosition = new ChessPosition(evalPosition.getRow() - 1, evalPosition.getColumn() - 1);
        }

//      Up and to the Left
        evalPosition = new ChessPosition(bishopPosition.getRow() + 1, bishopPosition.getColumn() - 1);
        while (ChessBoard.inBounds(evalPosition)) {

            if (chessBoard.isEmpty(evalPosition)) {
                ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                validMoves.add(newMove);
            } else {
                ChessPiece evalPiece = chessBoard.getPiece(evalPosition);
                ChessGame.TeamColor evalTeamColor = evalPiece.getTeamColor();

                if (bishop.getTeamColor() == evalTeamColor) {
                    break;
                } else if (bishop.getTeamColor() != evalTeamColor) {
                    ChessMove newMove = new ChessMove(bishopPosition, evalPosition, null);
                    validMoves.add(newMove);
                    break;
                }
            }
            evalPosition = new ChessPosition(evalPosition.getRow() + 1, evalPosition.getColumn() - 1);
        }

        return validMoves;
    }
}
