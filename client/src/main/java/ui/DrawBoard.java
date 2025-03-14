package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class DrawBoard {

    public static void main(String[] args) {
        var printOut = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String playerColor = "BLACK";
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

    private static void drawChessboard(PrintStream printOut, String playerColor, ChessBoard chessBoard) {
        drawHeader(printOut, playerColor);
        if (playerColor.equals("WHITE")) {
            for (int i = 8; i > 0; i--) {
                drawRow(printOut, playerColor, chessBoard, i);
            }
        } else {
            for (int i = 1; i < 9; i++) {
                drawRow(printOut, playerColor, chessBoard, i);
            }
        }
        drawHeader(printOut, playerColor);
    }

    private static void drawHeader(PrintStream printOut, String playerColor) {
        String header = "abcdefgh";
        printOut.print(SET_BG_COLOR_WHITE);
        printOut.print(SET_TEXT_COLOR_BLACK);
        if (playerColor.equals("WHITE")) {
            printOut.print(EMPTY);
            for(int i = 0; i < 8; i++) {
                char c = header.charAt(i);
                printOut.print(" " + c + " ");
            }
            printOut.print(EMPTY);
            printOut.println();
        } else {
            printOut.print(EMPTY);
            for(int i = 7; i >= 0; i--) {
                char c = header.charAt(i);
                printOut.print(" " + c + " ");
            }
            printOut.print(EMPTY);
            printOut.println();
        }
    }

    private static void drawRow(PrintStream printOut, String playerColor, ChessBoard chessBoard, int row) {
        drawRowNum(printOut, row);
        boolean whiteTile;
        if ((row % 2) == 0) {
            whiteTile = false;
        } else {
            whiteTile = true;
        }
        for (int i = 0; i < 8; i++) {
            if (whiteTile) {
                printOut.print(SET_BG_COLOR_LIGHT_GREY);
                whiteTile = false;
            } else {
                printOut.print(SET_BG_COLOR_DARK_GREY);
                whiteTile = true;
            }
            ChessPiece piece = chessBoard.getPiece(new ChessPosition(row, i));
            if (piece != null) {
                printOut.print(EMPTY);
            }
        }
        drawRowNum(printOut, row);
        printOut.println();
    }

    private static void drawRowNum(PrintStream printOut, int row) {
        printOut.print(SET_BG_COLOR_WHITE);
        printOut.print(SET_TEXT_COLOR_BLACK);
        printOut.print(" " + row + " ");
    }
}
