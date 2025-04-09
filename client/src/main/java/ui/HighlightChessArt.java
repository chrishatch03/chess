package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import chess.*;
import static ui.EscapeSequences.*;

public class HighlightChessArt extends ChessArtBase {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    private static final String BORDER_BG = SET_BG_COLOR_DARK_GREY;
    private static final String BLACK_BG = SET_BG_COLOR_BLACK;
    private static final String WHITE_BG = SET_BG_COLOR_WHITE;
    private static final String HIGHLIGHT_END = SET_BG_COLOR_YELLOW;
    private static final String HIGHLIGHT_START = SET_BG_COLOR_GREEN;

    private static final String BORDER_TEXT = SET_TEXT_COLOR_BLACK;
    private static final String BLACK_TEXT = SET_TEXT_COLOR_BLUE;
    private static final String WHITE_TEXT = SET_TEXT_COLOR_RED;


    public static void highlight(ChessBoard board, ChessGame.TeamColor playerColor, ChessPosition startPosition, ChessGame game) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        Collection<ChessMove> validMoves = game.validMoves(startPosition);
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove m : validMoves) {
            if (!m.getEndPosition().equals(startPosition)) {
                endPositions.add(m.getEndPosition());
            }
        }
        out.print("\n" + ERASE_SCREEN + SET_TEXT_BOLD);
        drawChessBoard(out, board, playerColor, endPositions, startPosition);
        out.print(RESET_BG_COLOR + RESET_TEXT_COLOR + RESET_TEXT_BOLD_FAINT);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor playerColor,
                                       Collection<ChessPosition> endPositions, ChessPosition startPosition) {
        drawTopBoarder(out, playerColor);
        if (playerColor == ChessGame.TeamColor.WHITE) {
            for (int boardRow = BOARD_SIZE_IN_SQUARES - 2; boardRow >= 1; --boardRow) {
                drawRowOfSquares(out, board, boardRow, playerColor, endPositions, startPosition);
            }
        } else {
            for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES - 1; ++boardRow) {
                drawRowOfSquares(out, board, boardRow, playerColor, endPositions, startPosition);
            }
        }
        drawTopBoarder(out, playerColor);
    }


    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int rowNum, ChessGame.TeamColor playerColor,
                                           Collection<ChessPosition> endPositions, ChessPosition startPosition) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (playerColor == ChessGame.TeamColor.BLACK) {
                for (int colNum = BOARD_SIZE_IN_SQUARES - 1; colNum >= 0; --colNum) {
                    printPieceSquare(colNum, rowNum, board, squareRow, out, playerColor, endPositions, startPosition);
                }
            } else {
                for (int colNum = 0; colNum < BOARD_SIZE_IN_SQUARES; ++colNum) {
                    printPieceSquare(colNum, rowNum, board, squareRow, out, playerColor, endPositions, startPosition);
                }
            }
            out.println();
        }
    }

    private static void printPieceSquare(int colNum, int rowNum, ChessBoard board, int squareRow,
                                           PrintStream out, ChessGame.TeamColor playerColor,
                                           Collection<ChessPosition> endPositions, ChessPosition startPosition) {
        String squareColor;
        String textColor;
        String paddedCharacter;
        ChessPosition currentPos = new ChessPosition(rowNum, colNum);

        if (colNum == 0 || colNum == 9) {
            squareColor = BORDER_BG;
            textColor = BORDER_TEXT;
            paddedCharacter = getPaddedRow(rowNum, playerColor);
        } else {
            ChessPiece piece = board.getPiece(currentPos);
            squareColor = getSquareColor(rowNum, colNum);
            textColor = (piece == null) ? RESET_TEXT_COLOR
                                        : (piece.getTeamColor() == ChessGame.TeamColor.BLACK) ? BLACK_TEXT : WHITE_TEXT;
            paddedCharacter = getPaddedPiece(piece);
        }

        if (currentPos.equals(startPosition)) {
            squareColor = HIGHLIGHT_START;
        } else {
            for (ChessPosition pos : endPositions) {
                if (currentPos.equals(pos)) {
                    squareColor = HIGHLIGHT_END;
                    break;
                }
            }
        }

        out.print(squareColor);
        out.print(textColor);
        printSquareRow(out, squareRow, paddedCharacter, squareColor, textColor);
    }



    private static String getPaddedRow(int rowNum, ChessGame.TeamColor playerColor) {
        return switch (rowNum) {
            case 1 -> " 1 ";
            case 2 -> " 2 ";
            case 3 -> " 3 ";
            case 4 -> " 4 ";
            case 5 -> " 5 ";
            case 6 -> " 6 ";
            case 7 -> " 7 ";
            case 8 -> " 8 ";
            default -> EMPTY;
        };
    }
}