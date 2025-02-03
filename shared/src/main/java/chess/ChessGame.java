package chess;

import java.util.Collection;
import java.util.ArrayList;

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
        Collection<ChessMove> confirmedMoves = new ArrayList<>();
        ChessPiece specifiedPiece = gameBoard.getPiece(startPosition);
        ChessGame.TeamColor specifiedPieceColor = specifiedPiece.getTeamColor();

        if (specifiedPiece != null) {
            Collection<ChessMove> validMoves = specifiedPiece.pieceMoves(gameBoard, startPosition);

            ChessBoard saveBoard = gameBoard.clone();
            for (ChessMove move: validMoves) {
                try {
                    makeMove(move);
                    confirmedMoves.add(move);
                } catch (InvalidMoveException e) {
//                    the move threw an InvalidMoveException which means the piece is somehow null... even though it shouldn't be possible because of the check on line 4 of this function
                    return null;
                } finally {
                    gameBoard = saveBoard;
                }
            }
            return confirmedMoves;
        } else {
            return null;
        }
    }

    /**
     * Checks each square in the path to make sure you're not jumping anything
     *
     * @param move chess move to perform
     */
    public boolean isPathClear(ChessMove move) {
        int startY = move.startPosition.getRow();
        int startX = move.startPosition.getColumn();
        int endY = move.endPosition.getRow();
        int endX = move.endPosition.getColumn();
        int yInc;
        int xInc;

        if (startY < endY) {
            yInc = +1;
        } else if (startY > endY) {
            yInc = -1;
        } else {
            yInc = 0;
        }

        if (startX < endX) {
            xInc = +1;
        } else if (startX > endX) {
            xInc = -1;
        } else {
            xInc = 0;
        }

        for (int row = startY, col = startX; row != endY || col != endX; row += yInc, col += xInc) {
            if (gameBoard.getPiece(new ChessPosition(row, col)) != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movePiece = gameBoard.getPiece(move.startPosition);
        ChessPiece.PieceType movePieceType = movePiece.getPieceType();
        ChessPiece takePiece = gameBoard.getPiece(move.endPosition);

        if (movePiece != null) {
            ChessGame.TeamColor movePieceColor = movePiece.getTeamColor();
//            Can't move if not your turn
            if (movePieceColor != teamTurn) {
                throw new InvalidMoveException();
            }
//            cannot take your own piece
            if (takePiece != null) {
                ChessGame.TeamColor takePieceColor = movePiece.getTeamColor();
                if (takePieceColor == movePieceColor) {
                    throw new InvalidMoveException();
                }
            }

            gameBoard.addPiece(move.startPosition, null);
            if (move.promotionPiece == null) {
                gameBoard.addPiece(move.endPosition, movePiece);
            } else {
                gameBoard.addPiece(move.endPosition, new ChessPiece(movePiece.getTeamColor(), move.promotionPiece));
            }
//            cannot move if in check after move
            if (isInCheck(movePieceColor)) {
                throw new InvalidMoveException();
            }
//              if QUEEN, ROOK, BISHOP, OR PAWN
            if (movePiece.getPieceType() == ChessPiece.PieceType.QUEEN || movePiece.getPieceType() == ChessPiece.PieceType.ROOK || movePiece.getPieceType() == ChessPiece.PieceType.BISHOP || movePiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (isPathClear(move) == false) {
                    throw new InvalidMoveException();
                }
            }
            PieceRulesValidator rulesValidator = null;
            switch (movePieceType) {
                case ChessPiece.PieceType.KING -> new KingRulesValidator();
            }
            if (!rulesValidator.isValidMove(gameBoard, move)) {
                throw new InvalidMoveException();
            }

            if (teamTurn == ChessGame.TeamColor.WHITE) {
                teamTurn = ChessGame.TeamColor.BLACK;
            } else {
                teamTurn = ChessGame.TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException();
        }
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
