package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition bishopPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessPiece bishop = board.getPiece(bishopPosition);
        ChessGame.TeamColor bishopColor = bishop.getTeamColor();

        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, 1, 1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, -1, 1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, -1, -1, board));
        validMoves.addAll(getDirectionMoves(bishopPosition, bishopColor, 1, -1, board));

        return validMoves;
    }

    public Collection<ChessMove> getDirectionMoves(ChessPosition bishopPosition, ChessGame.TeamColor bishopColor, int yIncrement, int xIncrement, ChessBoard board) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        ChessPosition evalPos = new ChessPosition(bishopPosition.getRow() + yIncrement, bishopPosition.getColumn() + xIncrement);
        while (ChessBoard.inBounds(evalPos)) {
            if (board.isEmpty(evalPos)) {
                validMoves.add(new ChessMove(bishopPosition, evalPos, null));
                evalPos = new ChessPosition(evalPos.getRow() + yIncrement, evalPos.getColumn() + xIncrement);
            } else if (board.getPiece(evalPos).getTeamColor() != bishopColor) {
                validMoves.add(new ChessMove(bishopPosition, evalPos, null));
                break;
            } else {
                break;
            }
        }

        return validMoves;
    }
}
