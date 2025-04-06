package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawBoard {

    ChessGame game;

    public DrawBoard(ChessGame game) {
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public void updateGame(ChessGame game) {
        this.game = game;
    }

    public void drawBoard(String playerColor, ChessPosition position) {
        var printOut = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Collection<ChessPosition> highlights = new ArrayList<>();
        if (position != null) {
            Collection<ChessMove> moves = game.validMoves(position);
            for (ChessMove validMove : moves) {
                highlights.add(validMove.getEndPosition());
            }
        }
        drawChessboard(printOut, playerColor, game.getBoard(), highlights);

        printOut.print(RESET_BG_COLOR);
        printOut.print(RESET_TEXT_COLOR);
    }

    private static void drawChessboard(PrintStream printOut, String playerColor,
                                       ChessBoard chessBoard, Collection<ChessPosition> highlights) {
        drawHeader(printOut, playerColor);
        if (playerColor.equals("WHITE")) {
            for (int i = 8; i > 0; i--) {
                drawRow(printOut, playerColor, chessBoard, i, highlights);
            }
        } else {
            for (int i = 1; i < 9; i++) {
                drawRow(printOut, playerColor, chessBoard, i, highlights);
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

    private static void drawRow(PrintStream printOut, String playerColor, ChessBoard chessBoard,
                                int row, Collection<ChessPosition> highlights) {
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
                whiteTile = tileSwitch(printOut, whiteTile);
                drawTile(printOut, chessBoard, row, i, highlights);
                printOut.print(RESET_TEXT_BOLD_FAINT);
            }
        } else {
            for (int i = 8; i > 0; i--) {
                whiteTile = tileSwitch(printOut, whiteTile);
                drawTile(printOut, chessBoard, row, i, highlights);
                printOut.print(RESET_TEXT_BOLD_FAINT);
            }
        }
        drawRowNum(printOut, row);
        printOut.println();
    }

    private static boolean tileSwitch(PrintStream printOut, boolean whiteTile) {
        if (whiteTile) {
            printOut.print(SET_BG_COLOR_LIGHT_GREY);
            whiteTile = false;
        } else {
            printOut.print(SET_BG_COLOR_DARK_GREY);
            whiteTile = true;
        }
        return whiteTile;
    }

    private static void drawRowNum(PrintStream printOut, int row) {
        printOut.print(SET_BG_COLOR_WHITE);
        printOut.print(SET_TEXT_COLOR_BLACK);
        printOut.print(" " + row + " ");
    }

    private static void drawTile(PrintStream printOut, ChessBoard chessBoard, int row,
                                 int col, Collection<ChessPosition> highlights) {
        ChessPosition position = new ChessPosition(row, col);
        if (highlights.contains(position)) {
            printOut.print(SET_BG_COLOR_GREEN);
        }
        ChessPiece piece = chessBoard.getPiece(position);
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
