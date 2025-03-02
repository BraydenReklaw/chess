package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] possibleMoves = {
                {1,0}, {0,1}, {-1,0}, {0,-1}
                // up, right, down, left
        };
        return SharedMoveLogic.queenRookBishopSharedLogic(possibleMoves, board, myPosition);
    }
}