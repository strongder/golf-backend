package com.example.golf.controller;

import com.example.golf.service.impl.ChatBotServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotServiceImpl chatBotServiceImpl;

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> handleDialogflowWebhook(@RequestBody Map<String, Object> request) {
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        String intentName = (String) ((Map<String, Object>) queryResult.get("intent")).get("displayName");

        String reply;

        switch (intentName) {
            case "AskAvailableServices":
                reply = chatBotServiceImpl.getAvailableServices().getMessage();
                break;
            case "AskPriceService":
                Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");
                String serviceName = (String) parameters.get("service-name");
                reply = String.valueOf(chatBotServiceImpl.getPriceService(serviceName));
                break;
            default:
                reply = "Xin lỗi, tôi chưa hiểu yêu cầu của bạn.";
                break;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", reply);

        return ResponseEntity.ok(response);
    }
}
