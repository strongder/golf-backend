package com.example.golf.repository;

import com.example.golf.enums.BookingStatus;
import com.example.golf.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b WHERE b.status =:status and (b.phone =:phone or b.bookingCode =:bookingCode)")
    Booking findBookingWhenCheckIn(
            @Value("status") String status,
            @Value("phone") String phone,
            @Value("bookingCode") String bookingCode
    );

    List<Booking> findAllByStatus(BookingStatus bookingStatus);
}
