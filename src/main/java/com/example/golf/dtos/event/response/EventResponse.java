package com.example.golf.dtos.event.response;

import com.example.golf.dtos.golf_course.response.DataFieldGolfCourse;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EventResponse {
    private String id;
    private String title;
    private String imageUrl; // URL of the event image
    private String description;
    private String type; // e.g. PROMOTION, TOURNAMENT
    private Double discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetUserType; // MEMBER, GUEST, STAFF, ALL
    private String status; // ACTIVE, INACTIVE
    private boolean isDeleted;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
