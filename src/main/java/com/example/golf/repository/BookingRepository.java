package com.example.golf.repository;

import com.example.golf.enums.BookingStatus;
import com.example.golf.model.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b WHERE b.status =:status and (b.phone =:phone or b.bookingCode =:bookingCode)")
    Booking findBookingWhenCheckIn(
            @Value("status") String status,
            @Value("phone") String phone,
            @Value("bookingCode") String bookingCode
    );

    List<Booking> findAllByStatus(BookingStatus bookingStatus);

    Optional<Booking>findByBookingCode(String bookingCode);



    //Truy van tong luot dat san
    @Query("SELECT COUNT(b) FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "AND (:golfCourseId = 'all' OR b.golfCourseId = :golfCourseId)")
    Long getTotalBookingsByStatus(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("golfCourseId") String golfCourseId);


    // Truy van tong odanh thu
    @Query("SELECT SUM(b.totalCost) FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "AND (:golfCourseId = 'all' OR b.golfCourseId = :golfCourseId)")
    Double getTotalRevenueByStatus(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("golfCourseId") String golfCourseId);



    // lay so luong booking theo ngay
    @Query("SELECT b.bookingDate, count(b) " +
            "FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "AND (:golfCourseId = 'all' OR b.golfCourseId = :golfCourseId) " +
            "GROUP BY b.bookingDate")
    List<Object[]> getNumberBooking(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("golfCourseId") String golfCourseId);

    // lay doanh thu theo ngay
    @Query("SELECT b.bookingDate, SUM(b.totalCost) " +
            "FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "AND (:golfCourseId = 'all' OR b.golfCourseId = :golfCourseId) " +
            "GROUP BY b.bookingDate")
    List<Object[]> getRevenueByDate(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("golfCourseId") String golfCourseId);


    // tuy van so luot dat tung san va thi phan doanh thu theo tung san
    @Query("SELECT b.golfCourseId, COUNT(b), SUM(b.totalCost) " +
            "FROM Booking b " +
            "WHERE b.status = 'COMPLETED' " +
            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "GROUP BY b.golfCourseId")
    List<Object[]> getBookingCountAndRevenueByGolfCourse(@Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

}
