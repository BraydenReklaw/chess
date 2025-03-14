package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class DrawBoard {

    public static void main(String[] args) {
        var printOut = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String playerColor = "WHITE";
        ChessBoard defaultBoard = new ChessBoard();
        defaultBoard.resetBoard();
        drawChessboard(printOut, playerColor, defaultBoard);

        printOut.print(RESET_BG_COLOR);
        printOut.print(RESET_TEXT_COLOR);
//        printOut.print(SET_BG_COLOR_RED);
//        printOut.print(" Hello I am Baymax ");
//        printOut.println();
//        printOut.print(SET_TEXT_BOLD);
//        printOut.print(" " + WHITE_KING + " ");
//        printOut.print(" " + BLACK_KING + " ");


    }

    private static void drawChessboard(PrintStream printOut, String playerColor, ChessBoard defaultBoard) {
        drawHeader(printOut, playerColor);
        for (int i = 0; i < 8; i++) {
            drawRow(printOut, playerColor, defaultBoard);
        }
        drawHeader(printOut, playerColor);
    }
}
