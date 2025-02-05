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

    ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        int row = 1;
        for (ChessPiece[] boardRow: gameBoard.squares) {
            int col = 1;
            for (ChessPiece piece: boardRow) {
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return new ChessPosition(row, col);
                }
                col ++;
            }
            row ++;
        }

        return null;
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

        if (specifiedPiece == null) {
            return null;
        }

        ChessBoard savedBoard = gameBoard.clone();
        ChessGame.TeamColor originalTurn = teamTurn;
        ChessGame.TeamColor specifiedPieceColor = specifiedPiece.getTeamColor();

        Collection<ChessMove> possibleMoves = specifiedPiece.pieceMoves(gameBoard, startPosition);

        for (ChessMove move: possibleMoves) {
            gameBoard = savedBoard.clone();
            teamTurn = specifiedPieceColor;

            try {

                makeMove(move);
                if (!isInCheck(specifiedPieceColor)) { confirmedMoves.add(move); }

            } catch (InvalidMoveException e) {
//                    swallow the move threw an InvalidMoveException
            }
        }

        teamTurn = originalTurn;
        gameBoard = savedBoard;
        return confirmedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard boardCopy = gameBoard.clone();
        ChessGame.TeamColor currTeamTurn = teamTurn;

        try {

            ChessPiece movePiece = gameBoard.getPiece(move.startPosition);
            ChessPiece takePiece = gameBoard.getPiece(move.endPosition);

            if (movePiece == null) { throw new InvalidMoveException(); }
            ChessPiece.PieceType movePieceType = movePiece.getPieceType();
            ChessGame.TeamColor movePieceColor = movePiece.getTeamColor();

            if (movePieceColor != teamTurn) {
                throw new InvalidMoveException();
            }
            if (takePiece != null) {
                ChessGame.TeamColor takePieceColor = takePiece.getTeamColor();
                if (takePieceColor == movePieceColor) {
                    throw new InvalidMoveException();
                }
            }
            PieceRulesValidator rulesValidator = getPieceRulesValidator(movePieceType);

            if (!rulesValidator.isValidMove(gameBoard, move)) {
                throw new InvalidMoveException();
            }

            gameBoard.addPiece(move.startPosition, null);
            if (move.promotionPiece == null) {
                gameBoard.addPiece(move.endPosition, movePiece);
            } else {
                gameBoard.addPiece(move.endPosition, new ChessPiece(movePieceColor, move.promotionPiece));
            }

            if (isInCheck(movePieceColor)) {
                throw new InvalidMoveException();
            }

            teamTurn = (teamTurn == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        } catch (InvalidMoveException e) {
            teamTurn = currTeamTurn;
            gameBoard = boardCopy;
            throw new InvalidMoveException();
        }

    }

    private static PieceRulesValidator getPieceRulesValidator(ChessPiece.PieceType movePieceType) {
        PieceRulesValidator rulesValidator = null;
        switch (movePieceType) {
            case ChessPiece.PieceType.KING -> rulesValidator = new KingRulesValidator();
            case ChessPiece.PieceType.QUEEN -> rulesValidator = new QueenRulesValidator();
            case ChessPiece.PieceType.ROOK -> rulesValidator = new RookRulesValidator();
            case ChessPiece.PieceType.BISHOP -> rulesValidator = new BishopRulesValidator();
            case ChessPiece.PieceType.PAWN -> rulesValidator = new PawnRulesValidator();
            case ChessPiece.PieceType.KNIGHT -> rulesValidator = new KnightRulesValidator();
        }
        return rulesValidator;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece currPiece = gameBoard.squares[row][col];
                if (currPiece == null || currPiece.getTeamColor() == teamColor) {
                    continue;
                }
                ChessPosition position = new ChessPosition(row + 1, col + 1);
                Collection<ChessMove> validMoves = currPiece.pieceMoves(gameBoard, position);

                for (ChessMove move: validMoves) {
                    if (kingPosition.equals(move.endPosition)) {
                        return true;
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

        if (!isInCheck(teamColor)) {
            return false;
        }

        return isStuck(teamColor);
    }

    private boolean isStuck(TeamColor teamColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece currPiece = gameBoard.getPiece(new ChessPosition(row + 1, col + 1));

                if (currPiece != null && currPiece.getTeamColor() == teamColor) {
                    ChessPosition position = new ChessPosition(row + 1, col + 1);
                    Collection<ChessMove> validMoves = validMoves(position);

                    if(!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if (isInCheck(teamColor)) {
            return false;
        }

        return isStuck(teamColor);
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
