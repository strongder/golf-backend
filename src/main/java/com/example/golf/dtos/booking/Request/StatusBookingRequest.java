package com.example.golf.dtos.booking.Request;

import com.example.golf.enums.BookingStatus;
import lombok.Data;

@Data
public class StatusBookingRequest {
    private String bookingId;
    private BookingStatus status;
}
