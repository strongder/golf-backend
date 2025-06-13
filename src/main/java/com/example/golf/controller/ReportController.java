package com.example.golf.controller;


import com.example.golf.service.impl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    public ReportServiceImpl reportService;

    @GetMapping("/total-bookings")
    public Long getTotalBookings(@RequestParam LocalDate startDate,
                                 @RequestParam LocalDate endDate,
                                 @RequestParam String golfCourseId) {
        return reportService.getTotalBookingsByStatus(startDate, endDate, golfCourseId);
    }
    @GetMapping("/total-revenue")
    public Double getTotalRevenue(@RequestParam LocalDate startDate,
                                  @RequestParam LocalDate endDate,
                                  @RequestParam String golfCourseId) {
        return reportService.getTotalRevenueByStatus(startDate, endDate, golfCourseId);
    }
    // so luong booking theo ngay
    @GetMapping("/number-booking")
    public List<Object[]> getNumberBooking(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate,
                                           @RequestParam String golfCourseId) {
        return reportService.getNumberBooking(startDate, endDate, golfCourseId);
    }

    @GetMapping("/revenue-by-date")
    public List<Object[]> getRevenueByDate(@RequestParam LocalDate startDate,
                                           @RequestParam LocalDate endDate,
                                           @RequestParam String golfCourseId) {
        return reportService.getRevenueByDate(startDate, endDate, golfCourseId);
    }
    @GetMapping("/booking-count-and-revenue-by-golf-course")
    public List<Object[]> getBookingCountAndRevenueByGolfCourse(@RequestParam LocalDate startDate,
                                                                @RequestParam LocalDate endDate
                                                                ) {
        return reportService.getBookingCountAndRevenueByGolfCourse(startDate, endDate);
    }
}
