package com.tqb2512.jchess.service;

import com.tqb2512.jchess.model.Game;
import com.tqb2512.jchess.model.GameStatus;
import com.tqb2512.jchess.model.User;
import com.tqb2512.jchess.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public Game createGame(User player1) {
        Game game = new Game(player1);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game joinGame(User player2, String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        game.setPlayer2(player2);
        return game;
    }

    public Map<String, Game> getGames() {
        return GameStorage.getInstance().getGames();
    }

    public Game startGame(String gameId) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        game.setCurrentPlayer(game.getPlayer1());
        game.setStatus(GameStatus.IN_PROGRESS);
        return game;
    }
}
