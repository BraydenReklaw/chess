import chess.*;
import ui.ServerFacade;
import ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        var facade = new ServerFacade(8810);
        facade.socketConnect();
        UI.preLogIn();
    }
}