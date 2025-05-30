package com.example.golf.dtos.event.request;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateEventRequest {
    private String title;
    private String description;
    private String type; // e.g. PROMOTION, TOURNAMENT
    private Double discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetUserType; // MEMBER, GUEST, STAFF, ALL
    private String status; // ACTIVE, INACTIVE
}
