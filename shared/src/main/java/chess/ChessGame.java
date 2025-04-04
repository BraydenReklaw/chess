package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameBoard;
    private TeamColor turnColor;
    private boolean gameOver;

    public ChessGame() {
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();
        this.turnColor = TeamColor.WHITE;
        this.gameOver = false;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public Boolean getGameOver() {
        return gameOver;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
        // throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turnColor = team;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) {
            return new ArrayList<>();
        }
        Collection<ChessMove> possible = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> valid = new ArrayList<>();

        for (ChessMove move : possible) {
            // Simulate move
            ChessPosition end = move.getEndPosition();
            ChessPiece capturedPiece = gameBoard.getPiece(end);

            gameBoard.addPiece(end, piece);
            gameBoard.addPiece(startPosition, null);

            // Check if the move leaves the king in check
            if (!isInCheck(piece.getTeamColor())) {
                valid.add(move);
            }

            // Undo move
            gameBoard.addPiece(startPosition, piece);
            gameBoard.addPiece(end, capturedPiece);
        }

        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = gameBoard.getPiece(start);

        if (piece == null) {
            throw new InvalidMoveException("Invalid Move, no piece present");
        }

        if (piece.getTeamColor() != turnColor) {
            throw new InvalidMoveException("Invalid Move, not your piece");
        }

        Collection<ChessMove> moves = validMoves(start);
        if (!moves.contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            ChessPiece promotion = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            gameBoard.addPiece(end, promotion);
        } else {
            gameBoard.addPiece(end, piece);
        }
        gameBoard.addPiece(start, null);
        if (turnColor == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Find the King
        ChessPosition kingPos = findKing(teamColor);
        if (kingPos == null) {
            throw new RuntimeException("King not found");
        }
        TeamColor opponent;
        if (teamColor == TeamColor.WHITE) {
            opponent = TeamColor.BLACK;
        } else {
            opponent = TeamColor.WHITE;
        }
        Collection<ChessMove> allOpponentMoves = findMoves(opponent);
        for (ChessMove move : allOpponentMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
            }
        }
        return false;
        //throw new RuntimeException("Not implemented");
    }

    private ChessPosition findKing(TeamColor teamColor) {
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPosition kingPos = new ChessPosition(r,c);
                ChessPiece king = gameBoard.getPiece(kingPos);
                if (king != null && (king.getPieceType() == ChessPiece.PieceType.KING && king.getTeamColor() == teamColor)) {
                    return kingPos;
                }
            }
        }
        return null;
    }
    private Collection<ChessMove> findMoves(TeamColor opponent){
        Collection<ChessMove> moves = new ArrayList<>();
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPosition pos = new ChessPosition(r,c);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() == opponent){
                    moves.addAll(piece.pieceMoves(gameBoard, pos));
                }
            }
        }
        return moves;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPosition pos = new ChessPosition(r,c);
                ChessPiece piece = gameBoard.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor){
                    if (!validMoves(pos).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
//        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            Collection<ChessMove> allMoves = findMoves(teamColor);
            for (ChessMove move : allMoves) {
                if (!validMoves(move.getStartPosition()).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;

//        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
        // throw new RuntimeException("Not implemented");
    }
}
