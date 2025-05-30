package com.example.golf.model;

import com.example.golf.enums.TeeTimeStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class TeeTime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String golfCourseId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer maxPlayers;
    private Integer bookedPlayers;
    @Enumerated(EnumType.STRING)
    private TeeTimeStatus status;
    private double price;
    private LocalDateTime heldAt;
    private String heldBy;
}
