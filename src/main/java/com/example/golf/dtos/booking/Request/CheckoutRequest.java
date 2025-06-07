package com.example.golf.dtos.booking.Request;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String bookingCode;
    private String notes;
    private String checkedOutBy;
}
