package com.example.golf.controller;

import com.example.golf.service.impl.ChatBotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotServiceImpl chatBotServiceImpl;


    @GetMapping("/services/price")
    public ResponseEntity<?> getServicePrice(@RequestParam String name) {
        return ResponseEntity.ok(chatBotServiceImpl.getPriceService(name));
    }

    @GetMapping("/services/available")
    public ResponseEntity<?> getAvailableTeeTimes() {
        return ResponseEntity.ok(chatBotServiceImpl.getAvailableServices());
    }


}
