package com.example.golf.dtos.booking.Request;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingSearchRequest extends BaseSearchRequest {
   private String userId;
   private String bookingCode;
   private String phone;
   private String golfCourseId;
   private LocalDate bookingDate;
   private String status;
}
