package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int[][] possibleMoves = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
                //up-right, right-up, right-down, down-right, down-left, left-down, left-up, up-left
        };
        return SharedMoveLogic.kingAndKnightLogic(possibleMoves, board, myPosition);
    }
}