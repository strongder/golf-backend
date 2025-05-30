package com.example.golf.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private String type; // e.g. PROMOTION, TOURNAMENT
    private Double discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetUserType; // MEMBER, GUEST, STAFF, ALL
    private String status; // ACTIVE, INACTIVE
}
