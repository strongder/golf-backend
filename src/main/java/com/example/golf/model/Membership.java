package com.example.golf.model;

import com.example.golf.enums.MembershipStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Membership extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String id;
    private String userId;
    private String code;
    private String membershipTypeId;
    private int bookingPerMonth;
    private LocalDate startDate; // YYYY-MM-DD
    private LocalDate endDate; // YYYY-MM-DD
    @Enumerated(EnumType.STRING)
    private MembershipStatus status;// 	PENDING, PAID,  ACTIVE, EXPIRED, CANCELLED
}
