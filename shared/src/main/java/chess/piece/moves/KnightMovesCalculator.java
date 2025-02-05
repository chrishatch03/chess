package chess.piece.moves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor knightColor = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition[] knightPositions = {
                new ChessPosition(row + 2, col + 1),
                new ChessPosition(row + 1, col + 2),
                new ChessPosition(row - 1, col + 2),
                new ChessPosition(row - 2, col + 1),
                new ChessPosition(row - 2, col - 1),
                new ChessPosition(row - 1, col - 2),
                new ChessPosition(row + 1, col - 2),
                new ChessPosition(row + 2, col - 1),
        };

        getKMoves(board, position, knightPositions, knightColor, validMoves);

        return validMoves;
    }

}
