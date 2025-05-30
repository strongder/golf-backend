package com.example.golf.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TeeTimeConfig extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String golfCourseId;
    private String dateType; // "weekend", "weekday"
    private LocalTime startTime;
    private String status; // "active", "inactive"
    private LocalTime endTime;
    private int maxPlayers; // Maximum number of slots available for booking
    private int duration; // Duration of the tee time in minutes
    private Double price; // Price per player
}
