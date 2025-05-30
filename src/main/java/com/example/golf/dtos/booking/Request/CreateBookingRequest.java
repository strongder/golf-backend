package com.example.golf.dtos.booking.Request;
import com.example.golf.dtos.booking_detail.request.BookingDetailRequest;
import com.example.golf.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateBookingRequest {
    private String id;
    private String fullName; // Tên người đặt sân
    private String phone; // Số điện thoại người đặt sân
    private String email; // Email người đặt sân
    private String golfCourseId;
    private String golferId;
    private String userId;
    private LocalDate bookingDate;
    private int numberOfHoles;
    private String teeTimeId;
    private int numPlayers;
    private double depositAmount;
    private double totalCost;
    private BookingStatus status;
    private String note;
}
