package com.example.golf.dtos.tee_time.response;

import lombok.Data;

@Data
public class TeeTimeResponse {
    private String id;
    private String golfCourseId;
    private String date;
    private String startTime;
    private int maxPlayers;
    private int bookedPlayers;
    private String status; // "available", "booked", "cancelled"
    private double price;
    private String heldAt; // ISO 8601 format
    private String heldBy; // User ID of the person who held the tee time
}
