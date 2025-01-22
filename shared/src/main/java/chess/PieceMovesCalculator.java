package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMovesCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    public Collection<ChessMove> getDirectionMoves(ChessPosition position, ChessGame.TeamColor teamColor, int yIncrement,
                                                   int xIncrement, ChessBoard board) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        ChessPosition evalPosition = new ChessPosition(position.getRow() + yIncrement,
                position.getColumn() + xIncrement);
        while (ChessBoard.inBounds(evalPosition)) {
            if (board.getPiece(evalPosition) != null) {
                if (board.getPiece(evalPosition).getTeamColor() != teamColor) {
                    validMoves.add(new ChessMove(position, evalPosition, null));
                    break;
                }
                break;
            }
            validMoves.add(new ChessMove(position, evalPosition, null));
            evalPosition = new ChessPosition(evalPosition.getRow() + yIncrement,
                    evalPosition.getColumn() + xIncrement);
        }

        return validMoves;
    }
}