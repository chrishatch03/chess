package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard gameBoard = new ChessBoard();
    ChessGame.TeamColor teamTurn = ChessGame.TeamColor.WHITE;

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece specifiedPiece = gameBoard.getPiece(startPosition);
        if (specifiedPiece != null) {
            Collection<ChessMove> validMoves = specifiedPiece.pieceMoves(gameBoard, startPosition);

            for (ChessMove move: validMoves) {
//                ChessBoard tempBoard = gameBoard.deepCopy();
            }
            return validMoves;
        } else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;

        for (int row = 0; row < gameBoard.squares.length; row++) {
            for (int col = 0; col < gameBoard.squares[row].length; col++) {

                ChessPosition position = new ChessPosition(row + 1, col + 1);
                ChessPiece currPiece = gameBoard.getPiece(position);
                if (currPiece != null) {
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == teamColor) {
                        kingPosition = position;
                    }
                }
            }
        }
        int kingX = kingPosition.getColumn();
        int kingY = kingPosition.getRow();
        for (int row = 0; row < gameBoard.squares.length; row++) {
            for (int col = 0; col < gameBoard.squares[row].length; col++) {

                ChessPosition position = new ChessPosition(row + 1, col + 1);
                ChessPiece currPiece = gameBoard.getPiece(position);
                if (currPiece != null && currPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> validMoves = currPiece.pieceMoves(gameBoard, position);
                    for (ChessMove move: validMoves) {
                        int endY = move.endPosition.getRow();
                        int endX = move.endPosition.getColumn();
                        if (endY == kingY && endX == kingX) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
