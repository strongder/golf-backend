package com.example.golf.dtos.tee_time_config.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.property.access.internal.PropertyAccessStrategyNoopImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class TeeTimeConfigRequest {
    private String golfCourseId;
    private String dateType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    private String maxPlayers;
    private int duration; // Duration of the tee time in minutes
    private String status; // "active", "inactive"
    private double price;
    private String createdBy; // ID of the user who created this config
    private String updatedBy; // ID of the user who last updated this config
}
