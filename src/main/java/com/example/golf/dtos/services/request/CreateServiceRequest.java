package com.example.golf.dtos.services.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateServiceRequest {
    private String name;
    private String code;
    private String type; // tool, other
    private String description;
    private MultipartFile image; // URL of the service image
    private String price;
    private String status; // "ACTIVE", "INACTIVE"
}
