package com.example.golf.model;

import com.example.golf.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "service")
public class Services extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String code;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private ServiceType type; // OTHER, SINGLE_CLUB, CLUB_SET, TOOL, CADDIE
    private String name;
    private String description;
    private double price;
    private String status; // active, inactive
}
