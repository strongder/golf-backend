package com.example.golf.dtos.booking_detail.response;

import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.dtos.tool.response.ToolResponse;
import lombok.Data;

@Data
public class BookingDetailResponse {
    private String id;
    private String bookingId;
    private ServicesResponse service;
    private ToolResponse tool;
    private String serviceId;
    private String toolId;
    private int quantity;
    private double unitPrice; // Giá tiền đơn vị
    private double totalPrice; // Giá tiền tổng cộng
}
