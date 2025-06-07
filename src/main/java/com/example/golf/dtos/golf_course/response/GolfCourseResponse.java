package com.example.golf.dtos.golf_course.response;

import com.example.golf.enums.GolfCourseStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GolfCourseResponse {
    private String id;
    private String code;
    private String name;
    private String imageUrl;
    private int holes;
    private int duration; // thoi gian choi
    private float length; // do dai san golf
    private String description;
    private String location;
    private GolfCourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDelete;
    private String createdBy;
    private String updatedBy;
}
