package chess;

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
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
    }

    public static boolean inBounds(ChessPosition position) {
        var x = position.getColumn();
        var y = position.getRow();
        if (x > 7 || x < 0) {
            System.out.println(String.format("Col boundary error, (%s,%s) not in range.", position.getColumn(), position.getRow()));
            return false;
        }

        if (y > 7 || y < 0) {
            System.out.println(String.format("Row boundary error, (%s,%s) not in range.", position.getColumn(), position.getRow()));
            return false;
        }

        return true;
    }

//    public static boolean isEmpty(ChessPosition position) {
//        if (self.getPiece(position)) {
//
//        }
//    }

}
