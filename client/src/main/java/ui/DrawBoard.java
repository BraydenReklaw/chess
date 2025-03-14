package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {

    public static void DrawBoard(String playerColor, ChessBoard defaultBoard) {
        var printOut = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawChessboard(printOut, playerColor, defaultBoard);

        printOut.print(RESET_BG_COLOR);
        printOut.print(RESET_TEXT_COLOR);
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
//            printOut.print(EMPTY);
            printOut.print("   ");
            for(int i = 0; i < 8; i++) {
                char c = header.charAt(i);
                printOut.print(" " + c + " ");
            }
//            printOut.print(EMPTY);
            printOut.print("   ");
            printOut.println();
        } else {
//            printOut.print(EMPTY);
            printOut.print("   ");
            for(int i = 7; i >= 0; i--) {
                char c = header.charAt(i);
                printOut.print(" " + c + " ");
            }
//            printOut.print(EMPTY);
            printOut.print("   ");
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
        printOut.print(SET_TEXT_COLOR_WHITE);
        if (playerColor.equals("WHITE")) {
            whiteTile = !whiteTile;
            for (int i = 1; i < 9; i++) {
                if (whiteTile) {
                    printOut.print(SET_BG_COLOR_LIGHT_GREY);
                    whiteTile = false;
                } else {
                    printOut.print(SET_BG_COLOR_DARK_GREY);
                    whiteTile = true;
                }
                drawTile(printOut, chessBoard, row, i);
                printOut.print(RESET_TEXT_BOLD_FAINT);
            }
        } else {
            for (int i = 8; i > 0; i--) {
                if (whiteTile) {
                    printOut.print(SET_BG_COLOR_LIGHT_GREY);
                    whiteTile = false;
                } else {
                    printOut.print(SET_BG_COLOR_DARK_GREY);
                    whiteTile = true;
                }
                drawTile(printOut, chessBoard, row, i);
                printOut.print(RESET_TEXT_BOLD_FAINT);
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

    private static void drawTile(PrintStream printOut, ChessBoard chessBoard, int row, int col) {
        ChessPiece piece = chessBoard.getPiece(new ChessPosition(row, col));
        if (piece == null) {
//            printOut.print(EMPTY);
            printOut.print("   ");
        } else {
            String printArg = piece.getTeamColor() + "_" + piece.getPieceType();
            if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                printOut.print(SET_TEXT_COLOR_WHITE);
            } else {
                printOut.print(SET_TEXT_COLOR_RED);
                printOut.print(SET_TEXT_BOLD);
            }
            switch (printArg) {
//                case "WHITE_KING" -> printOut.print(WHITE_KING);
//                case "WHITE_QUEEN" -> printOut.print(WHITE_QUEEN);
//                case "WHITE_ROOK" -> printOut.print(WHITE_ROOK);
//                case "WHITE_BISHOP" -> printOut.print(WHITE_BISHOP);
//                case "WHITE_KNIGHT" -> printOut.print(WHITE_KNIGHT);
//                case "WHITE_PAWN" -> printOut.print(WHITE_PAWN);
//                case "BLACK_KING" -> printOut.print(BLACK_KING);
//                case "BLACK_QUEEN" -> printOut.print(BLACK_QUEEN);
//                case "BLACK_ROOK" -> printOut.print(BLACK_ROOK);
//                case "BLACK_KNIGHT" -> printOut.print(BLACK_KNIGHT);
//                case "BLACK_BISHOP" -> printOut.print(BLACK_BISHOP);
//                case "BLACK_PAWN" -> printOut.print(BLACK_PAWN);
                case "WHITE_KING" -> printOut.print(" K ");
                case "WHITE_QUEEN" -> printOut.print(" Q ");
                case "WHITE_ROOK" -> printOut.print(" R ");
                case "WHITE_BISHOP" -> printOut.print(" B ");
                case "WHITE_KNIGHT" -> printOut.print(" N ");
                case "WHITE_PAWN" -> printOut.print(" P ");
                case "BLACK_KING" -> printOut.print(" K ");
                case "BLACK_QUEEN" -> printOut.print(" Q ");
                case "BLACK_ROOK" -> printOut.print(" R ");
                case "BLACK_KNIGHT" -> printOut.print(" N ");
                case "BLACK_BISHOP" -> printOut.print(" B ");
                case "BLACK_PAWN" -> printOut.print(" P ");
            }
        }
    }
}
