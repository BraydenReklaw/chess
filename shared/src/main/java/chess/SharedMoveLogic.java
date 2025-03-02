package chess;

import java.util.ArrayList;
import java.util.Collection;

public class SharedMoveLogic {

    public static Collection<ChessMove> kingAndKnightLogic(int [][] possibleMoves, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i < possibleMoves.length; i++) {
            int[] possibleMove = possibleMoves[i];
            int row = myPosition.getRow() + possibleMove[0];
            int col = myPosition.getColumn() + possibleMove[1];
            if (board.isValidMove(row, col)) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null || (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }

        }
        return moves;
    }

    public static Collection<ChessMove> queenRookBishopSharedLogic(int [][] possibleMoves, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 0; i < possibleMoves.length; i++) {
            int[] possibleMove = possibleMoves[i];
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            while (true) {
                row += possibleMove[0];
                col += possibleMove[1];
                if (!board.isValidMove(row, col)) {
                    break;
                }
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(newPosition);
                if (piece == null) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
                else {
                    if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                    break;
                }
            }

        }
        return moves;
    }
}
