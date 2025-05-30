package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.booking_detail.request.BookingDetailRequest;
import com.example.golf.service.BookingDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking-detail")
public class BookingDetailController {

    @Autowired
    private BookingDetailService bookingDetailService;

    @GetMapping("/booking/{bookingId}")
    public ApiResponse getDetailByBookingId(@PathVariable("bookingId") String bookingId) {
        return ApiResponse.success(bookingDetailService.getDetailByBooking(bookingId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse softDelete(@PathVariable("id") String id) {
        return ApiResponse.success(bookingDetailService.softDelete(id));
    }


    @PostMapping("/booking/{bookingId}")
    public ApiResponse addBookingDetailToBooking(@PathVariable("bookingId") String bookingId,
                                                 @RequestBody List<BookingDetailRequest> request) {
        bookingDetailService.createBookingDetail(bookingId, request);
        return ApiResponse.success("Booking detail added successfully");
    }
}
