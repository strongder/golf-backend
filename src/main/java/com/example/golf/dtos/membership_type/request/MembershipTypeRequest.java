package com.example.golf.dtos.membership_type.request;


import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
public class MembershipTypeRequest {
    private String name;
    private double price; // in USD
    private int duration; // in months
    private int maxBookingPerMonth; // maximum number of bookings allowed per mont
    private int bookingBeforeDay; // number of days before booking allowed)
    private List<String> benefits; // comma-separated list of benefits
    private int discount; // percentage discount for members
}
