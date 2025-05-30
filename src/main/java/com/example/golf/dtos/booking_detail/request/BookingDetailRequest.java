package com.example.golf.dtos.booking_detail.request;

import lombok.Data;

@Data
public class BookingDetailRequest{
    private String id;
    private String serviceId;
    private String toolId;
    private int quantity;
    private double totalPrice; // Giá tiền tổng cộng
}
