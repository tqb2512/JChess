package com.tqb2512.jchess.storage;

import com.tqb2512.jchess.model.Game;

import java.util.Map;

public class GameStorage {

    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        games = new java.util.HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getId(), game);
    }
}

