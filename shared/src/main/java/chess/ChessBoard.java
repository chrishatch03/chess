package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessPosition normalizedPosition = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        squares[normalizedPosition.getRow()][normalizedPosition.getColumn()] = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        ChessPosition normalizedPosition = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        return squares[normalizedPosition.getRow()][normalizedPosition.getColumn()];
    }

    public boolean isEmpty(ChessPosition position) {
            ChessPiece piece = this.getPiece(position);

            if (piece != null) {
                switch (piece.getPieceType()) {
                    case BISHOP, QUEEN, KING, ROOK, PAWN, KNIGHT -> { return false; }
                }
                return true;
            }
            return true;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
    }

    public static boolean inBounds(ChessPosition position) {
        var row = position.getRow();
        var col = position.getColumn();

        if (row > 8 || row < 1) {
//            System.out.println(String.format("Row boundary error, (%s,%s) not in range.", row, col));
            return false;
        }

        if (col > 8 || col < 1) {
//            System.out.println(String.format("Col boundary error, (%s,%s) not in range.", row, col));
            return false;
        }

        return true;
    }

    /***
     * Returns a string representation
     *
     * @return String
     */
    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
