package com.example.golf.model;

import com.example.golf.enums.StaffRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Staff extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String code;
    private String email;
    private String phone;
    private String avatar;
    private String dob;
    private String gender;
    private String userId;
    private String fullName;
    private String address;
    @Enumerated(EnumType.STRING)
    private StaffRole role;  // RECEPTIONIST, CADDY, MAINTENANCE, MANAGER
}
