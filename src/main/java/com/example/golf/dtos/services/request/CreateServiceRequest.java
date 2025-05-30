package com.example.golf.dtos.services.request;

import lombok.Data;

@Data
public class CreateServiceRequest {
    private String name;
    private String type; // tool, other
    private String description;
    private String price;
    private String status; // "ACTIVE", "INACTIVE"
}
