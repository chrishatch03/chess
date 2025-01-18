package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> getDirectionMoves(ChessPosition rookPosition, ChessGame.TeamColor rookColor, int yIncrement, int xIncrement, ChessBoard board) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        ChessPosition evalPos = new ChessPosition(rookPosition.getRow() + (yIncrement), rookPosition.getColumn() + (xIncrement));
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + (yIncrement), evalPos.getColumn() + (xIncrement));
            } else if (board.getPiece(evalPos).getTeamColor() != rookColor) {
                validMoves.add(new ChessMove(rookPosition, evalPos, null));
                break;
            } else {
                break;
            }
        }

        return validMoves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition rookPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor rookColor = board.getPiece(rookPosition).getTeamColor();

        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 1, 0, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 0, 1, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, -1, 0, board));
        validMoves.addAll(getDirectionMoves(rookPosition, rookColor, 0, -1, board));

        return validMoves;
    }
}
