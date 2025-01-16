package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition chessPosition) {
        var x = chessPosition.getColumn();
        var y = chessPosition.getRow();

//        get moves going up and to the left
        ArrayList<ChessMove> diagUpLeft = new ArrayList<ChessMove>();
        ChessPosition oldPosition = chessPosition;
        while (ChessBoard.inBounds(oldPosition)) {
            ChessPosition newPosition = new ChessPosition(oldPosition.getRow() + 1, oldPosition.getColumn() - 1);
            ChessMove newMove = new ChessMove(chessPosition, newPosition, null);
            diagUpLeft.add(newMove);
            oldPosition = newPosition;
        }

        ArrayList<ChessMove> diagUpRight = new ArrayList<ChessMove>();
        oldPosition = chessPosition;
        while (ChessBoard.inBounds(oldPosition)) {
            ChessPosition newPosition = new ChessPosition(oldPosition.getRow() + 1, oldPosition.getColumn() + 1);
            ChessMove newMove = new ChessMove(chessPosition, newPosition, null);
            diagUpRight.add(newMove);
            oldPosition = newPosition;
        }

        ArrayList<ChessMove> diagDownLeft = new ArrayList<ChessMove>();
        oldPosition = chessPosition;
        while (ChessBoard.inBounds(oldPosition)) {
            ChessPosition newPosition = new ChessPosition(oldPosition.getRow() - 1, oldPosition.getColumn() - 1);
            ChessMove newMove = new ChessMove(chessPosition, newPosition, null);
            diagDownLeft.add(newMove);
            oldPosition = newPosition;
        }

        ArrayList<ChessMove> diagDownRight = new ArrayList<ChessMove>();
        oldPosition = chessPosition;
        while (ChessBoard.inBounds(oldPosition)) {
            ChessPosition newPosition = new ChessPosition(oldPosition.getRow() - 1, oldPosition.getColumn() + 1);
            ChessMove newMove = new ChessMove(chessPosition, newPosition, null);
            diagDownRight.add(newMove);
            oldPosition = newPosition;
        }

        ArrayList<ChessMove> chessMoves = new ArrayList<ChessMove>();
        for(int i = 0; i < diagUpLeft.size(); i++) {
            chessMoves.add(diagUpLeft.get(i));
        }
        for(int i = 0; i< diagUpRight.size(); i++) {
            chessMoves.add(diagUpRight.get(i));
        }
        for(int i = 0; i < diagDownLeft.size(); i++) {
            chessMoves.add(diagDownLeft.get(i));
        }
        for(int i = 0; i< diagDownRight.size(); i++) {
            chessMoves.add(diagDownRight.get(i));
        }
        return chessMoves;
    }
}
