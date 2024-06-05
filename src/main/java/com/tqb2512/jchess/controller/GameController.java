package com.tqb2512.jchess.controller;

import com.tqb2512.jchess.model.Game;
import com.tqb2512.jchess.model.GameStatus;
import com.tqb2512.jchess.model.User;
import com.tqb2512.jchess.service.GameService;
import com.tqb2512.jchess.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/createRoom")
    public ResponseEntity<Game> createRoom(@RequestBody User player1) {
        Game game = gameService.createGame(player1);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @RequestMapping("/getRoom")
    public ResponseEntity<Game> getGame(@RequestParam String gameId) {
        Game game = gameService.getGames().get(gameId);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @RequestMapping("/getRooms")
    public ResponseEntity<Map<String, Game>> getGames() {
        return ResponseEntity.ok(gameService.getGames());
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<Game> joinRoom(@RequestBody User player2, @RequestParam String gameId) {
        Game game = gameService.joinGame(player2, gameId);
        gameService.startGame(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return ResponseEntity.ok(game);
    }

    @RequestMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam String gameId) {
        Game game = gameService.startGame(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return ResponseEntity.ok(game);
    }

    @RequestMapping("/move")
    public ResponseEntity<Game> move(@RequestParam String gameId, @RequestParam int selectCol, @RequestParam int selectRow, @RequestParam int targetCol, @RequestParam int targetRow) {
        Game game = gameService.move(gameId, selectCol, selectRow, targetCol, targetRow);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/leave")
    public ResponseEntity<Game> leaveRoom(@RequestParam String gameId, @RequestBody User user) {
        Game game = gameService.getGames().get(gameId);
        userService.updateStats(user.getUsername(), false);
        userService.updateStats(game.getPlayer1().getUsername().equals(user.getUsername()) ? game.getPlayer2().getUsername() : game.getPlayer1().getUsername(), true);
        game.setStatus(GameStatus.FINISHED);
        gameService.removeGame(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return ResponseEntity.ok(game);
    }

    @RequestMapping("/remove")
    public ResponseEntity<Game> removeRoom(@RequestParam String gameId) {
        Game game = gameService.getGames().get(gameId);
        gameService.removeGame(gameId);
        return ResponseEntity.ok(game);
    }
}
