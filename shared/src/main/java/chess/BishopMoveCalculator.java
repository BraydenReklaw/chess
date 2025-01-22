package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] possibleMoves = {
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
                // up-right, down-right, down-left, up-left
        };

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

        };
        return moves;
    }
}