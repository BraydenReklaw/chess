package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameDataAccess {
    private List<GameData> games = new ArrayList<>();

    public GameDataAccess() {
        this.games = games;
    }

    public Collection<GameData> listAll() {
        return games;
    }

    public void clearAll() {
        games.clear();
    }
}
