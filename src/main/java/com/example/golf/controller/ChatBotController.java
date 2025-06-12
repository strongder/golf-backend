package com.example.golf.controller;

import com.example.golf.service.impl.ChatBotServiceImpl;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    @PostMapping("/mobile")
    public ResponseEntity<String> chatWithBot(@RequestBody Map<String, String> body) throws Exception {
        String projectId = "golfbot-hfcf";
        String sessionId = UUID.randomUUID().toString();
        String text = body.get("message");
        String languageCode = "vi";

        SessionsClient sessionsClient = SessionsClient.create(
                SessionsSettings.newBuilder()
                        .setCredentialsProvider(() ->
                                GoogleCredentials.fromStream(new FileInputStream("dialog-key.json"))
                        ).build());

        SessionName session = SessionName.of(projectId, sessionId);

        TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
        String reply = response.getQueryResult().getFulfillmentText();
        return ResponseEntity.ok(reply);
    }

}
