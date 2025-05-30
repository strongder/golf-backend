package com.example.golf.dtos.tee_time.response;

import com.example.golf.enums.TeeTimeStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TeeTimeRequest {
    private String id;
    private String golfCourseId;
    private LocalDate date;
    private LocalTime startTime;
    private Integer maxPlayers;
    private Integer bookedPlayers;
    private TeeTimeStatus status;
    private double price;
}
