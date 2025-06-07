package com.example.golf.controller;

import com.example.golf.service.impl.ChatBotServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotServiceImpl chatBotServiceImpl;

    @PostMapping("/webhook")
    public ResponseEntity<Map<String, Object>> handleDialogflowWebhook(@RequestBody Map<String, Object> request) {
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        Map<String, Object> parameters = (Map<String, Object>) queryResult.get("parameters");
        String intentName = (String) ((Map<String, Object>) queryResult.get("intent")).get("displayName");

        String reply;

        switch (intentName) {
            case "AskOpeningHours":
                reply = chatBotServiceImpl.getOpeningHours().getMessage();
                break;
            case "AskLocation":
                reply = chatBotServiceImpl.getAddress().getMessage();
                break;
            case "AskContact":
                reply = chatBotServiceImpl.getContactInfo().getMessage();
                break;
            case "AskAvailableServices":
                reply = chatBotServiceImpl.getAvailableServices().getMessage();
                break;
            case "AskAvailableTeeTime":
                String dateStr = (String) parameters.get("date");
                String timeStr = (String) parameters.get("time");

                OffsetDateTime odt = OffsetDateTime.parse(dateStr);
                OffsetDateTime timedt = OffsetDateTime.parse(timeStr);

                LocalDate date = odt.toLocalDate();
                LocalTime time = null;
                if (timeStr != null && !timeStr.isEmpty()) {
                    time = timedt.toLocalTime();
                }
                reply = chatBotServiceImpl.getAvailableTeeTimes(date, time).getMessage();
                break;
            case "AskServicePrice":
                String serviceName = (String) parameters.get("name");

                reply = chatBotServiceImpl.getPriceService(serviceName).getMessage();
                break;
            default:
                reply = "Xin lỗi, tôi không hiểu yêu cầu của bạn.";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", reply);

        return ResponseEntity.ok(response);
    }
}
