package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import chess.*;
import static ui.EscapeSequences.*;

public abstract class ChessArtBase {

    static final int BOARD_SIZE_IN_SQUARES = 10;
    static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    static final String BORDER_BG = SET_BG_COLOR_DARK_GREY;
    static final String BLACK_BG = SET_BG_COLOR_BLACK;
    static final String WHITE_BG = SET_BG_COLOR_WHITE;

    static final String BORDER_TEXT = SET_TEXT_COLOR_BLACK;
    static final String BLACK_TEXT = SET_TEXT_COLOR_BLUE;
    static final String WHITE_TEXT = SET_TEXT_COLOR_RED;


    static void drawTopBoarder(PrintStream out, ChessGame.TeamColor playerColor) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            String[] columnLabels = new String[]{EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY};
            setBorder(out);

            if (playerColor == ChessGame.TeamColor.WHITE) {

                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    setBorder(out);
                    printSquareRow(out, squareRow, columnLabels[boardCol], BORDER_BG, BORDER_TEXT);
                }

            } else {

                for (int boardCol = BOARD_SIZE_IN_SQUARES -1 ; boardCol >= 0; --boardCol) {
                    setBorder(out);
                    printSquareRow(out, squareRow, columnLabels[boardCol], BORDER_BG, BORDER_TEXT);
                }

            }

            setReset(out);

            out.println();
        }
    }

    static void printSquareRow(PrintStream out, int squareRow, String label, String bgColor, String textColor) {
        if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
            int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
            int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

            out.print(EMPTY.repeat(prefixLength));
            printSquareContent(out, label, bgColor, textColor);
            out.print(EMPTY.repeat(suffixLength));
        }
        else {
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
        setReset(out);
    }

    static void printSquareContent(PrintStream out, String paddedCharacter, String bgColor, String textColor) {
        out.print(bgColor);
        out.print(textColor);
        out.print(paddedCharacter);
    }

    static void setBorder(PrintStream out) {
        out.print(BORDER_BG);
        out.print(BORDER_TEXT);
    }

    static void setReset(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    static String getSquareColor(int rowNum, int colNum) {
        int evenRow = rowNum % 2;
        int evenCol = colNum % 2;
        return switch (evenRow) {
            case 0 -> (evenCol == 1) ? WHITE_BG : BLACK_BG;
            case 1 -> (evenCol == 1) ? BLACK_BG : WHITE_BG;
            default -> BORDER_BG;
        };
    }

    static String getPaddedPiece(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        ChessPiece.PieceType pieceType = piece.getPieceType();

        return switch (pieceType) {
            case KING -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_KING : WHITE_KING;
            case QUEEN -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_QUEEN : WHITE_QUEEN;
            case ROOK -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_ROOK : WHITE_ROOK;
            case BISHOP -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_BISHOP : WHITE_BISHOP;
            case KNIGHT -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_KNIGHT : WHITE_KNIGHT;
            case PAWN -> (pieceColor == ChessGame.TeamColor.BLACK) ? BLACK_PAWN : WHITE_PAWN;
        };
    }
}