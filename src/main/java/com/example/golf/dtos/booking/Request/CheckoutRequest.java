package com.example.golf.dtos.booking.Request;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String bookingId;
    private String notes;
    private String checkedOutBy;
}
