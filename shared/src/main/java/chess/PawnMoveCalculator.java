package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] possibleWhiteMoves = {
                {1,0}, //up
                {1,-1}, //attack left
                {1,1}, //attack right

        };
        int [][] possibleBlackMoves = {
                {-1,0}, //down
                {-1,-1}, // attack-left
                {-1,1}, // attack-right
        };

        ChessPiece myPawn = board.getPiece(myPosition);
        int[][] possibleMoves = myPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? possibleWhiteMoves : possibleBlackMoves;

        for (int i = 0; i < possibleMoves.length; i++) {
            int[] possibleMove = possibleMoves[i];
            int row = myPosition.getRow() + possibleMove[0];
            int col = myPosition.getColumn() + possibleMove[1];
            ChessPosition newPosition = new ChessPosition(row, col);
            boolean promote = false;
            boolean start = false;
            if (row == 8 || row == 1) {
                promote = true;
            }
            if (row == 2 || row == 7) {
                start = true;
            }
            if (!promote) {
                if (board.isValidMove(row, col)) {
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null && i == 0) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                        if (start) {
                            // implement advance position here
                            piece = board.getPiece(newPosition + );
                            if (piece == null && i == 0) {
                                moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                    if (i != 0 && piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }


//            if (i == 0) {
//                // pawn forward moves from start space
//                if (myPosition.getRow() == 2) {
//                    if (board.getPiece(newPosition) == null) {
//                        moves.add(new ChessMove(myPosition, newPosition, null));
//                        ChessPosition pawnsprint = new ChessPosition(row + 1, col);
//                        if (board.getPiece(pawnsprint) == null || (board.getPiece(pawnsprint).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
//                            moves.add(new ChessMove(myPosition, pawnsprint, null));
//                        }
//                    }
//                }
//                else {
//                    if (board.getPiece(newPosition) == null && !promote) {
//                        moves.add(new ChessMove(myPosition, newPosition, null));
//                    }
//                }
//            }

        }
        return moves;
    }
}
