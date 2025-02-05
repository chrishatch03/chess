package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor kingColor = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition[] kingPositions = {
                new ChessPosition(row + 1, col),
                new ChessPosition(row + 1, col + 1),
                new ChessPosition(row, col + 1),
                new ChessPosition(row - 1, col + 1),
                new ChessPosition(row - 1, col),
                new ChessPosition(row - 1, col - 1),
                new ChessPosition(row, col - 1),
                new ChessPosition(row + 1, col - 1),
        };

        getKMoves(board, position, kingPositions, kingColor, validMoves);

        return validMoves;
    }

}
