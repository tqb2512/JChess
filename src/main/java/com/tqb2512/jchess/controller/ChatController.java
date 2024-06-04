package com.tqb2512.jchess.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqb2512.jchess.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/send")
    public void sendChatMessage(@RequestParam String roomId, @RequestParam String message, @RequestBody User user) {
        Map<String, String> chatMessage = new HashMap<>();
        chatMessage.put("username", user.getUsername());
        chatMessage.put("message", message);
        try {
            simpMessagingTemplate.convertAndSend("/topic/chat/" + roomId, new ObjectMapper().writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            log.error("Error sending chat message", e);
        }
    }
}
