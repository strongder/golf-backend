package com.example.golf.service.impl;

import com.example.golf.dtos.ChatbotResponse;
import com.example.golf.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatBotServiceImpl {
    @Autowired
    ServicesRepository servicesRepository;


    public ChatbotResponse getAvailableServices() {
        ChatbotResponse chatbotResponse = new ChatbotResponse();
        StringBuilder responseMessage = new StringBuilder("Here are the available services:\n");

        servicesRepository.findAll().forEach(service -> {
            responseMessage.append("- ").append(service.getName()).append("\n");
        });

        chatbotResponse.setMessage(responseMessage.toString());
        return chatbotResponse;
    }

    public ChatbotResponse getPriceService(String serviceName) {
        return ChatbotResponse.builder()
                .message(servicesRepository.getPriceByService(serviceName))
                .build();
    }
}
