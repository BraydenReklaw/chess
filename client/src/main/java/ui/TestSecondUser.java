package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class TestSecondUser {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var game = new ChessGame();
        var gameUI = new GameUI(game);
        var facade = new ServerFacade(8810);
        facade.socketConnect();
        facade.setObserver(gameUI);
        UI.preLogIn(gameUI);
    }
}
