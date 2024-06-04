package com.tqb2512.jchess.controller;

import com.tqb2512.jchess.model.Game;
import com.tqb2512.jchess.model.User;
import com.tqb2512.jchess.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
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
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @RequestMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam String gameId) {
        Game game = gameService.startGame(gameId);
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }
}
