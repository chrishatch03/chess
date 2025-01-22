package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition kingPosition) {
        var x = kingPosition.getColumn();
        var y = kingPosition.getRow();
        ChessPiece king = chessBoard.getPiece(kingPosition);

        ChessPosition[] possiblePositions = {
                new ChessPosition(y + 1, x), //             Top
                new ChessPosition(y + 1, x + 1), //     TopRight
                new ChessPosition(y, x + 1), //              Right
                new ChessPosition(y - 1, x + 1), //     BottomRight
                new ChessPosition(y - 1, x), //             Bottom
                new ChessPosition(y - 1, x - 1), //     BottomLeft
                new ChessPosition(y, x - 1), //              Left
                new ChessPosition(y + 1, x - 1) //      TopLeft
        };

        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();

        for (ChessPosition position: possiblePositions) {
            if (ChessBoard.inBounds(position)) {
                if (chessBoard.isEmpty(position)) {
                    validMoves.add(new ChessMove(kingPosition, position, null));
                } else {

//                    Check that the existingPiece's team color is not the same as the king
//                    If not, add the move since it's valid
                    ChessPiece existingPiece = chessBoard.getPiece(position);
                    if (existingPiece.getTeamColor() == king.getTeamColor()) {
                        continue;
                    } else {
                        validMoves.add(new ChessMove(kingPosition, position, null));
                    }
                }
            }
        }

        return validMoves;
    }
}
