package dataaccess;

import model.GameData;

import java.util.*;

public class InMemoryGameDAO {
    private final Map<Integer, GameData> games = new HashMap<>();
    private boolean failOnClear = false;

    public void insertGame(GameData game) {
        games.put(game.getGameID(), game);
    }

    public Collection<GameData> getAllGames() {
        return games.values();
    }

    public void setFailOnClear(boolean value) {
        this.failOnClear = value;
    }

    public void clear() {
        if (failOnClear) throw new RuntimeException("Clear failed");
        games.clear();
    }
}

