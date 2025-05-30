package com.example.golf.dtos.membership_type.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MembershipTypeResponse {
    private String id;
    private String name;
    private double price; // in USD
    private int duration; // in months
    private int maxBookingPerMonth; // maximum number of bookings allowed per mont
    private int bookingBeforeDay; // number of days before booking allowed)
    private List<String> benefits; // comma-separated list of benefits
    private int discount;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
