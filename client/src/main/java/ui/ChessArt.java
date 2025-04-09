package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.*;

import static ui.EscapeSequences.*;

public class ChessArt extends ChessArtBase {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    private static final String BORDER_BG = SET_BG_COLOR_DARK_GREY;
    private static final String BLACK_BG = SET_BG_COLOR_BLACK;
    private static final String WHITE_BG = SET_BG_COLOR_WHITE;

    private static final String BORDER_TEXT = SET_TEXT_COLOR_BLACK;
    private static final String BLACK_TEXT = SET_TEXT_COLOR_BLUE;
    private static final String WHITE_TEXT = SET_TEXT_COLOR_RED;


    public static void draw(ChessBoard board, ChessGame.TeamColor playerColor) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print("\n");
        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_BOLD);

        drawChessBoard(out, board, playerColor);

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
    }


    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor playerColor) {
        drawTopBoarder(out, playerColor);
        if (playerColor == ChessGame.TeamColor.WHITE) {

            for (int boardRow = BOARD_SIZE_IN_SQUARES - 2; boardRow >= 1; --boardRow) {
                drawRowOfSquares(out, board, boardRow, playerColor);
            }

        } else {

            for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES - 1; ++boardRow) {
                drawRowOfSquares(out, board, boardRow, playerColor);
            }
        }
        drawTopBoarder(out, playerColor);

    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int rowNum, ChessGame.TeamColor playerColor) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {

            if (ChessGame.TeamColor.BLACK == playerColor) {
                for (int colNum = BOARD_SIZE_IN_SQUARES -1; colNum >= 0; --colNum) {

                    printPieceSquare(colNum, rowNum, board, squareRow, out, playerColor);

                }

            } else {

                for (int colNum = 0; colNum < BOARD_SIZE_IN_SQUARES; ++colNum) {

                    printPieceSquare(colNum, rowNum, board, squareRow, out, playerColor);

                }

            }

            out.println();
        }
    }

    private static void printPieceSquare(int colNum, int rowNum, ChessBoard board, int squareRow, PrintStream out, ChessGame.TeamColor playerColor) {
        String squareColor;
        String textColor;
        String paddedCharacter;
        if (colNum == 0 || colNum == 9) {
            squareColor = BORDER_BG;
            textColor = BORDER_TEXT;
            paddedCharacter = getPaddedRow(rowNum, playerColor);
        } else {
            ChessPiece piece = board.getPiece(new ChessPosition(rowNum, colNum));
            squareColor = getSquareColor(rowNum, colNum);
            textColor = (piece == null) ? RESET_TEXT_COLOR : (piece.getTeamColor() == ChessGame.TeamColor.BLACK) ? BLACK_TEXT : WHITE_TEXT;
            paddedCharacter = getPaddedPiece(piece);
        }

        out.print(squareColor);
        out.print(textColor);

        printSquareRow(out, squareRow, paddedCharacter, squareColor, textColor);
    }


    private static String getPaddedRow(int rowNum, ChessGame.TeamColor playerColor) {
        return switch (rowNum) {
            case 1 -> ROW1;
            case 2 -> ROW2;
            case 3 -> ROW3;
            case 4 -> ROW4;
            case 5 -> ROW5;
            case 6 -> ROW6;
            case 7 -> ROW7;
            case 8 -> ROW8;
            default -> EMPTY;
        };
    }

}
