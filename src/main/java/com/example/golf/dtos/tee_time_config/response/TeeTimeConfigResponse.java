package com.example.golf.dtos.tee_time_config.response;


import com.example.golf.dtos.golf_course.response.DataFieldGolfCourse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeeTimeConfigResponse {

    private String id;
    private String golfCourseId;
    private DataFieldGolfCourse golfCourse;
    private String dateType;
    private String startTime;
    private String endTime;
    private int maxPlayers;
    private int duration; // Duration of the tee time in minutes
    private String status; // "active", "inactive"
    private double price;
    private String createdBy; // ID of the user who created this config
    private String updatedBy; // ID of the user who last updated this config
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
