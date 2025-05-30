package com.example.golf.service;

import com.example.golf.dtos.booking_detail.request.BookingDetailRequest;
import com.example.golf.dtos.booking_detail.response.BookingDetailResponse;
import com.example.golf.model.BookingDetail;

import java.util.List;

public interface BookingDetailService extends BaseService<BookingDetail, String> {
    void createBookingDetail(String bookingId, List<BookingDetailRequest> request);
    void updateBookingDetail(String bookingId, List<BookingDetailRequest> request);
    List<BookingDetailResponse> getDetailByBooking(String bookingId);
    String softDelete(String bookingDetailId);
}
