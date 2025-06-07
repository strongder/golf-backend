package com.example.golf.dtos.services.response;


import lombok.Data;

@Data
public class ServicesResponse {
    private String id;
    private String code;
    private String type; // tool, other
    private String name;
    private String imageUrl; // URL of the service image
    private String description;
    private double price;
    private String status; // active, inactive
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
}
