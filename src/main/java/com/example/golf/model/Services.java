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
    private String type; // GOLF_CLUB, GOLF_BALL, GOLF_BAG, GOLF_CART, GOLF_SHOES, GOLF_GLOVE, GOLF_TEE
    private String name;
    private String description;
    private double price;
    private String status; // active, inactive
}
