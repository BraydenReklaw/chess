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
            // check if pawn can promote
            if (row == 8 || row == 1) {
                promote = true;
            }
            // check if pawn has yet to take a move
            if ((row == 3 && possibleMoves ==possibleWhiteMoves) || (row == 6 && possibleMoves == possibleBlackMoves)) {
                start = true;
            }
            // Non Promoting Pawns
            if (!promote) {
                if (board.isValidMove(row, col)) {
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null && i == 0) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                        // double move case
                        if (start) {
                            if (possibleMoves == possibleWhiteMoves) {
                                row += 1;
                            }
                            else {
                                row -= 1;
                            }
                            ChessPosition first = new ChessPosition(row, col);
                            piece = board.getPiece(first);
                            if (piece == null) {
                                moves.add(new ChessMove(myPosition, first, null));
                            }
                        }
                    }
                    // attack case
                    if (i != 0 && piece != null) {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }
            // Promotion cases
            else {
                if (board.isValidMove(row, col)) {
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null && i == 0) {
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    }
                    if (i != 0 && piece != null) {
                        if (piece.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
