package com.example.golf.repository;

import com.example.golf.dtos.booking_detail.request.BookingDetailRequest;
import com.example.golf.model.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {

    List<BookingDetail> findByBookingId(String bookingId);

    @Query("SELECT COALESCE(SUM(bd.totalPrice), 0) FROM BookingDetail bd WHERE bd.bookingId = :bookingId")
    double sumTotalPriceByBookingId(@Param("bookingId") String bookingId);
}
