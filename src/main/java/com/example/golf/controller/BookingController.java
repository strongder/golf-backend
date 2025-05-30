package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.booking.Request.*;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Create booking")
    @PostMapping("/create")
    private ApiResponse createBooking(@RequestBody CreateBookingRequest request) {
        return ApiResponse.success(bookingService.createBooking(request));
    }

    @Operation(summary = "Create list booking")
    @PostMapping("/save-all")
    private ApiResponse saveAll(@RequestBody List<CreateBookingRequest> request) {
        return ApiResponse.success(bookingService.saveAll(request));
    }

    @Operation(summary = "Create booking")
    @PutMapping("/{bookingId}")
    private ApiResponse updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody UpdateBookingRequest request) {
        return ApiResponse.success(bookingService.updateBooking(bookingId, request));
    }

    @Operation(summary = "Checkin booking")
    @PostMapping("/check-in/{bookingId}")
    private ApiResponse checkIn(@PathVariable("bookingId") String bookingId) {
        return ApiResponse.success(bookingService.checkIn(bookingId));
    }


    @Operation(summary = "Get booking by id")
    @PostMapping("/search")
    private ApiResponse search(@RequestBody BookingSearchRequest request) {
        return ApiResponse.success(bookingService.search(request));
    }

    @Operation(summary = "Change status of booking")
    @PostMapping("/change-status")
    public ApiResponse changeStatus(@RequestBody StatusBookingRequest request) {
        return ApiResponse.success(bookingService.updateStatus(request));
    }

    @Operation(summary = "Check out booking")
    @PostMapping("/check-out")
    public ApiResponse checkOut(@RequestBody CheckoutRequest request) {
        return ApiResponse.success(bookingService.checkOut(request));
    }
}
