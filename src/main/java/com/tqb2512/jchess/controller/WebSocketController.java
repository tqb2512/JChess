package com.tqb2512.jchess.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.tqb2512.jchess.model.Game;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/game/{gameId}")
    public void handleGameUpdate(@DestinationVariable String gameId, Game game) {
        simpMessagingTemplate.convertAndSend("/topic/game/" + gameId, game);
    }

    @MessageMapping("/chat/{gameId}/")
    public void handleChatMessage(@DestinationVariable String gameId, String message) {
        simpMessagingTemplate.convertAndSend("/topic/chat/" + gameId, message);
    }
}