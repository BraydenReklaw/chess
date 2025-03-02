package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] possibleMoves = {
                {1,1}, {1,-1}, {-1,1}, {-1,-1}
                // up-right, down-right, down-left, up-left
        };
        return SharedMoveLogic.queenRookBishopSharedLogic(possibleMoves, board, myPosition);
    }
}