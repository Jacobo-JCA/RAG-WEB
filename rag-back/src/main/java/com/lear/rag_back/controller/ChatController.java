package com.lear.rag_back.controller;

import com.lear.rag_back.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        if(question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La pregunta no puede estar vacia"));
        }
        String response = chatService.answerQuestion(question);
        return ResponseEntity.ok(Map.of("respuesta", response));
    }
}
