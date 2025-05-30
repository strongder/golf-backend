package com.example.golf.model;

import com.example.golf.enums.GuestType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "guest")
public class Guest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate; // Format: YYYY-MM-DD
    private String gender;
    @Enumerated(EnumType.STRING)
    private GuestType role;// "member", "guest"
    private String userId;
    private int totalBooking;
}