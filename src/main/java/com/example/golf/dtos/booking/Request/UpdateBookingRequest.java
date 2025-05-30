package com.example.golf.dtos.booking.Request;

import com.example.golf.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UpdateBookingRequest {
    private String fullName;
    private String phone;
    private String email;
    private LocalDate bookingDate;
    private int numPlayers;
    private int numberOfHoles;
    private String teeTimeId;
    private String note;
    private BookingStatus status; // Nếu cần thay đổi trạng thái (tuỳ vào luồng nghiệp vụ)
}
