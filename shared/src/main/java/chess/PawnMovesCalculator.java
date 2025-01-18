package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class PawnMovesCalculator implements PieceMovesCalculator {

//    fully implemented should return the user's chosen piece, not a queen
    public ChessPiece getPromotionPiece() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Which piece would you like to promote your pawn to? ");
        String chosenPiece = scanner.nextLine();
        System.out.println(String.format("Chosen %s", chosenPiece));
        return new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    }


    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pawnPosition) {
        int y = pawnPosition.getRow();
        int x = pawnPosition.getColumn();

        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(pawnPosition);
        ChessGame.TeamColor pawnColor = pawn.getTeamColor();

        ArrayList<ChessPosition> possiblePositions = new ArrayList<ChessPosition>();

        if (pawnColor == ChessGame.TeamColor.WHITE) {

            ChessPosition upOne = new ChessPosition(y + 1, x);
            if (ChessBoard.inBounds(upOne) && board.isEmpty(upOne)) {

                validMoves.add(new ChessMove(pawnPosition, upOne, null));
                if (pawnPosition.getRow() == 2) {
                    ChessPosition upTwo = new ChessPosition(y + 2, x);
                    if (ChessBoard.inBounds(upTwo) && board.isEmpty(upTwo)) {
                        validMoves.add(new ChessMove(pawnPosition, upTwo, null));
                    }
                }

            }

            ChessPosition upRight = new ChessPosition(y + 1, x + 1);
            if (ChessBoard.inBounds(upRight)) {

                if (!board.isEmpty(upRight)) {

                    ChessPosition landingPos = new ChessPosition(y + 2,x + 2);
                    if (ChessBoard.inBounds(landingPos)) {

                        if (board.isEmpty(landingPos)) {
                            validMoves.add(new ChessMove(pawnPosition, landingPos, null));
                        }
                    }
                }
            }

            ChessPosition upLeft = new ChessPosition(y + 1, x - 1);
            if (ChessBoard.inBounds(upLeft)) {

                if (!board.isEmpty(upLeft)) {

                    ChessPosition landingPos = new ChessPosition(y + 2,x - 2);
                    if (ChessBoard.inBounds(landingPos)) {

                        if (board.isEmpty(landingPos)) {
                            validMoves.add(new ChessMove(pawnPosition, landingPos, null));
                        }
                    }
                }
            }

        } else if (pawnColor == ChessGame.TeamColor.BLACK) {

            ChessPosition downOne = new ChessPosition(y - 1, x);
            if (ChessBoard.inBounds(downOne) && board.isEmpty(downOne)) {

                validMoves.add(new ChessMove(pawnPosition, downOne, null));
                if (pawnPosition.getRow() == 2) {
                    ChessPosition downTwo = new ChessPosition(y - 2, x);
                    if (ChessBoard.inBounds(downTwo) && board.isEmpty(downTwo)) {
                        validMoves.add(new ChessMove(pawnPosition, downTwo, null));
                    }
                }

            }

            ChessPosition downRight = new ChessPosition(y - 1, x + 1);
            if (ChessBoard.inBounds(downRight)) {

                if (!board.isEmpty(downRight)) {

                    ChessPosition landingPos = new ChessPosition(y - 2,x + 2);
                    if (ChessBoard.inBounds(landingPos)) {

                        if (board.isEmpty(landingPos)) {
                            validMoves.add(new ChessMove(pawnPosition, landingPos, null));
                        }
                    }
                }
            }

            ChessPosition downLeft = new ChessPosition(y - 1, x - 1);
            if (ChessBoard.inBounds(downLeft)) {

                if (!board.isEmpty(downLeft)) {

                    ChessPosition landingPos = new ChessPosition(y - 2,x - 2);
                    if (ChessBoard.inBounds(landingPos)) {

                        if (board.isEmpty(landingPos)) {
                            validMoves.add(new ChessMove(pawnPosition, landingPos, null));
                        }
                    }
                }
            }

        }

//        if (pawnColor == ChessGame.TeamColor.WHITE) {
//
//            possiblePositions.add(new ChessPosition(y + 1, x));
//            if (pawn.getNumMoves() == 0) {
//                possiblePositions.add(new ChessPosition(y + 2, x));
//            }
//            possiblePositions.add(new ChessPosition(y + 1, x + 1));
//            possiblePositions.add(new ChessPosition(y + 1, x - 1));
//
//        } else if (pawnColor == ChessGame.TeamColor.BLACK) {
//
//            possiblePositions.add(new ChessPosition(y - 1, x));
//            if (pawn.getNumMoves() == 0) {
//                possiblePositions.add(new ChessPosition(y - 2, x));
//            }
//            possiblePositions.add(new ChessPosition(y - 1, x + 1));
//            possiblePositions.add(new ChessPosition(y - 1, x - 1));
//
//        }
//
//        ArrayList<ChessPosition> validPositions = new ArrayList<ChessPosition>();
//        for (ChessPosition evalPosition: possiblePositions) {
//            if (!ChessBoard.inBounds(evalPosition)) { continue; }
//
//            if (board.isEmpty(evalPosition)) {
//                validPositions.add(evalPosition);
//            }
//
//        }


        return validMoves;
    }
}
