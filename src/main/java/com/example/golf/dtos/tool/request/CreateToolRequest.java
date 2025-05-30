package com.example.golf.dtos.tool.request;

import com.example.golf.enums.ToolStatus;
import lombok.Data;

@Data
public class CreateToolRequest {
    private String code;
    private String name;
    private int quantity;
    private String type;  // golf_club, golf_cart, other
    private ToolStatus status;
    private double rentPrice;
    private String provider;
}
