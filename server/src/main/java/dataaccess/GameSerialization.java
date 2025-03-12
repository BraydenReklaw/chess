package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;

public class GameSerialization {

    String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    ChessGame deserializeGame(String serializedGame) {
        return new Gson().fromJson(serializedGame, ChessGame.class);
    }
}
