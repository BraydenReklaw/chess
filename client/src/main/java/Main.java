import chess.*;

//import server.Server;
//import ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        // these lines are for testing the ui
//        Server server = new Server();
//        var port = server.run(8810);
//        System.out.println("Started test HTTP server on " + port);
//        UI.UI();
//        server.stop();
    }
}