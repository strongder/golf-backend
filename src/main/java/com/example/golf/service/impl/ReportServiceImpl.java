package com.example.golf.service.impl;

import com.example.golf.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl {

    @Autowired
    private BookingRepository reportRepository;


    //Truy van tong luot dat san
//    @Query("SELECT COUNT(b) FROM Booking b " +
//            "WHERE b.status = 'COMPLETED' " +
//            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
//            "AND (:golfCourseId IS NULL OR b.golfCourseId = :golfCourseId)")
//    Long getTotalBookingsByStatus(@Param("startDate") LocalDate startDate,
//                                  @Param("endDate") LocalDate endDate,
//                                  @Param("golfCourseId") String golfCourseId);
//
//
//    // Truy van tong odanh thu
//    @Query("SELECT SUM(b.totalCost) FROM Booking b " +
//            "WHERE b.status = 'COMPLETED' " +
//            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
//            "AND (:golfCourseId IS NULL OR b.golfCourseId = :golfCourseId)")
//    Double getTotalRevenueByStatus(@Param("startDate") LocalDate startDate,
//                                   @Param("endDate") LocalDate endDate,
//                                   @Param("golfCourseId") String golfCourseId);
//
//
//
//    //lay booking theo tung ngay va doanh thu theo tung ngay
//    @Query("SELECT b.bookingDate, SUM(b.totalCost) " +
//            "FROM Booking b " +
//            "WHERE b.status = 'COMPLETED' " +
//            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
//            "AND (:golfCourseId IS NULL OR b.golfCourseId = :golfCourseId) " +
//            "GROUP BY b.bookingDate")
//    List<Object[]> getRevenueByDate(@Param("startDate") LocalDate startDate,
//                                    @Param("endDate") LocalDate endDate,
//                                    @Param("golfCourseId") String golfCourseId);
//
//
//    // tuy van so luot dat tung san va thi phan doanh thu theo tung san
//    @Query("SELECT b.golfCourseId, COUNT(b), SUM(b.totalCost) " +
//            "FROM Booking b " +
//            "WHERE b.status = 'COMPLETED' " +
//            "AND b.bookingDate BETWEEN :startDate AND :endDate " +
//            "GROUP BY b.golfCourseId")
//    List<Object[]> getBookingCountAndRevenueByGolfCourse(@Param("startDate") LocalDate startDate,
//                                                         @Param("endDate") LocalDate endDate);

    // viet cac service theo cac truy van tren
    public Long getTotalBookingsByStatus(LocalDate startDate, LocalDate endDate, String golfCourseId) {
        return reportRepository.getTotalBookingsByStatus(startDate, endDate, golfCourseId);
    }

    public Double getTotalRevenueByStatus(LocalDate startDate, LocalDate endDate, String golfCourseId) {
        return reportRepository.getTotalRevenueByStatus(startDate, endDate, golfCourseId);
    }

    // lay so luong bookng theo ngay
    public List<Object[]> getNumberBooking(LocalDate startDate, LocalDate endDate, String golfCourseId) {
        return reportRepository.getNumberBooking(startDate, endDate, golfCourseId);
    }

    public List<Object[]> getRevenueByDate(LocalDate startDate, LocalDate endDate, String golfCourseId) {
        return reportRepository.getRevenueByDate(startDate, endDate, golfCourseId);
    }

    public List<Object[]> getBookingCountAndRevenueByGolfCourse(LocalDate startDate, LocalDate endDate) {
        return reportRepository.getBookingCountAndRevenueByGolfCourse(startDate, endDate);
    }
}
