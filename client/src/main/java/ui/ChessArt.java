package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.*;

import static ui.EscapeSequences.*;

public class ChessArt {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    // My Colors
    
    private static String BORDER_BG = SET_BG_COLOR_DARK_GREY;
    private static String BLACK_BG = SET_BG_COLOR_BLACK;
    private static String WHITE_BG = SET_BG_COLOR_WHITE;
    
    private static String BORDER_TEXT = SET_TEXT_COLOR_BLACK;
    private static String BLACK_TEXT = SET_TEXT_COLOR_BLUE;
    private static String WHITE_TEXT = SET_TEXT_COLOR_RED;

    // Padded Characters in EscapeSequences.*


    public static void draw(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_BOLD);

        drawChessBoard(out, board);

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
    }


    private static void drawChessBoard(PrintStream out, ChessBoard board) {

        drawTopBoarder(out);

        for (int boardRow = 1; boardRow < BOARD_SIZE_IN_SQUARES - 1; ++boardRow) {
            drawRowOfSquares(out, board, boardRow);
        }

        drawTopBoarder(out);

    }

    private static void drawTopBoarder(PrintStream out) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            String[] columnLabels = new String[]{EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY};
            setBorder(out);
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printSquareContent(out, columnLabels[boardCol], BORDER_BG, BORDER_TEXT);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

            }
            setReset(out);

            out.println();
        }
    }

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int rowNum) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int colNum = 0; colNum < BOARD_SIZE_IN_SQUARES; ++colNum) {
                
                String squareColor;
                String textColor;
                String paddedCharacter;
                if (colNum == 0 || colNum == 9) {
                    // char is the row number
                    // boarder bg
                    // boarder text
                    squareColor = BORDER_BG;
                    textColor = BORDER_TEXT;
                    paddedCharacter = getPaddedRow(rowNum);

                } else {
                    // char is piece char
                    // square color depends on getSquareColor
                    // team text color
                    ChessPiece piece = board.getPiece(new ChessPosition(rowNum, colNum));
                    squareColor = getSquareColor(rowNum, colNum);
                    // textColor line should probably be refactored
                    textColor = (piece == null) ? RESET_TEXT_COLOR : (piece.getTeamColor() == ChessGame.TeamColor.BLACK) ? BLACK_TEXT : WHITE_TEXT;
                    paddedCharacter = getPaddedPiece(piece);
                }
                
                out.print(squareColor);
                out.print(textColor);

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                    

                    out.print(EMPTY.repeat(prefixLength));
                    printSquareContent(out, paddedCharacter, squareColor, textColor);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                setReset(out);
            }

            out.println();
        }
    }

    private static String getPaddedPiece(ChessPiece piece) {
        if (piece == null) return EMPTY;
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

    private static String getPaddedRow(int rowNum) {
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

    private static String getSquareColor(int rowNum, int colNum) {

        //odd row & even column is white. odd row & odd column is black.
        // even row & even column is black. even row & odd column is white.
        int evenRow = rowNum % 2;
        int evenCol = colNum % 2;
        return switch (evenRow) {
            case 0 -> (evenCol == 1) ? WHITE_BG : BLACK_BG;
            case 1 -> (evenCol == 1) ? BLACK_BG : WHITE_BG;
            default -> BORDER_BG;
        };

    }


    private static void setWhite(PrintStream out) {
        out.print(WHITE_BG);
        out.print(WHITE_TEXT);
    }

    private static void setBlack(PrintStream out) {
        out.print(BLACK_BG);
        out.print(BLACK_TEXT);
    }

    private static void setBorder(PrintStream out) {
        out.print(BORDER_BG);
        out.print(BORDER_TEXT);
    }

    private static void setReset(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printSquareContent(PrintStream out, String paddedCharacter, String bgColor, String textColor) {
        out.print(bgColor);
        out.print(textColor);
        out.print(paddedCharacter);

        // setWhite(out);
    }

}
