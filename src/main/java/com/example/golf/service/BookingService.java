package com.example.golf.service;

import com.example.golf.dtos.booking.Request.*;
import com.example.golf.dtos.booking.Response.BookingResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.BookingStatus;
import com.example.golf.model.Booking;
import org.springframework.transaction.annotation.Transactional;

public interface BookingService extends BaseService<Booking, String> {
   BookingResponse createBooking(CreateBookingRequest request);
   String updateStatus(StatusBookingRequest request);
   BookingResponse updateBooking(String bookingId, UpdateBookingRequest request);
   String softDelete(String bookingId);
   BaseSearchResponse<BookingResponse> search(BookingSearchRequest request);
   BookingResponse checkOut(String bookingCode, String paymentMethod);
   void changeStatus(String bookingId, BookingStatus status);
   Object checkIn(String bookingCode);
}
