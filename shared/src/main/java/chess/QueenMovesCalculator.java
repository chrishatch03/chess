package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition queenPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor queenColor = board.getPiece(queenPosition).getTeamColor();

        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, 0, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 0, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, 1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, 0, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, -1, -1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 0, -1, board));
        validMoves.addAll(getDirectionMoves(queenPosition, queenColor, 1, -1, board));

        return validMoves;
    }

    public Collection<ChessMove> getDirectionMoves(ChessPosition queenPosition, ChessGame.TeamColor queenColor, int yIncrement, int xIncrement, ChessBoard board) {
        ArrayList<ChessMove> validQueenMoves = new ArrayList<>();

        ChessPosition evalPos = new ChessPosition(queenPosition.getRow() + yIncrement, queenPosition.getColumn() + xIncrement);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                validQueenMoves.add(new ChessMove(queenPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + yIncrement, evalPos.getColumn() + xIncrement);
            } else if (board.getPiece(evalPos).getTeamColor() != queenColor) {
                validQueenMoves.add(new ChessMove(queenPosition, evalPos, null));
                break;
            } else {
                break;
            }
        }

        return validQueenMoves;
    }

}
