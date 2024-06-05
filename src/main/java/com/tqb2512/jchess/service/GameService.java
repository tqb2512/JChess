package com.tqb2512.jchess.service;

import com.tqb2512.jchess.model.Game;
import com.tqb2512.jchess.model.GameStatus;
import com.tqb2512.jchess.model.Square;
import com.tqb2512.jchess.model.User;
import com.tqb2512.jchess.model.piece.Piece;
import com.tqb2512.jchess.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class GameService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

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

    public Game move(String gameId, int selectCol, int selectRow, int targetCol, int targetRow) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        game.move(selectCol, selectRow, targetCol, targetRow);
        if (game.isCheckMate()) {
            game.setStatus(GameStatus.FINISHED);
            userService.updateStats(game.getCurrentPlayer().getUsername().equals(game.getPlayer1().getUsername()) ? game.getPlayer2().getUsername() : game.getPlayer1().getUsername(), true);
            userService.updateStats(game.getCurrentPlayer().getUsername(), false);
            removeGame(gameId);
        }
        return game;
    }

    public void removeGame(String gameId) {
        GameStorage.getInstance().getGames().remove(gameId);
    }
}
