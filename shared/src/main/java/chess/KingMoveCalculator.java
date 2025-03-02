package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int [][] possibleMoves = {
                {1,0}, {0,1}, {-1,0}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}
                // up, right, down, left, up-right, down-right, down-left, up-left
        };
        return SharedMoveLogic.kingAndKnightLogic(possibleMoves,board,myPosition);
    }
}
