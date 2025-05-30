package com.example.golf.model;


import com.example.golf.enums.GolfCourseStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "golf_course")
public class GolfCourse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String image;
    private String code;
    private String name;
    private int holes;
    private float length; // do dai san golf
    private String description;
    private String location;
    private int duration; // thoi gian choi
    @Enumerated(EnumType.STRING)
    private GolfCourseStatus status; // NORMAL, MAINTENANCE, CLOSED
}