package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.*;

import static ui.EscapeSequences.*;

public class ChessArt {

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

    private static void drawTopBoarder(PrintStream out, ChessGame.TeamColor playerColor) {
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
                    printSquareRow(out, squareRow, columnLabels[boardCol], BORDER_BG, BORDER_TEXT);
                }

            }

            setReset(out);

            out.println();
        }
    }

    private static void printSquareRow(PrintStream out, int squareRow, String label, String bgColor, String textColor) {
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

    private static String getPaddedPiece(ChessPiece piece) {
        if (piece == null) { return EMPTY; }
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

    private static String getPaddedRow(int rowNum, ChessGame.TeamColor playerColor) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
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
        } else {
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

    private static String getSquareColor(int rowNum, int colNum) {

        int evenRow = rowNum % 2;
        int evenCol = colNum % 2;
        return switch (evenRow) {
            case 0 -> (evenCol == 1) ? WHITE_BG : BLACK_BG;
            case 1 -> (evenCol == 1) ? BLACK_BG : WHITE_BG;
            default -> BORDER_BG;
        };

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
    }

}
