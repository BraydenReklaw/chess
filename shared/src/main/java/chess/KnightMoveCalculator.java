package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[][] possibleMoves = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
                //up-right, right-up, right-down, down-right, down-left, left-down, left-up, up-left
        };

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
}