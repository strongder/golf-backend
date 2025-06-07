package com.example.golf.dtos.tool.request;

import com.example.golf.enums.ToolStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateToolRequest {
    private String code;
    private String name;
    private int quantity;
    private MultipartFile image; // URL of the tool image
    private String type;  // golf_club, golf_cart, other
    private ToolStatus status;
    private double rentPrice;
    private String provider;
}
