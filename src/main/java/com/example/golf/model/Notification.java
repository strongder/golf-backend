package com.example.golf.model;

import com.example.golf.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title; // Tiêu đề thông báo
    private String content; // Nội dung thông báo
    @Enumerated(EnumType.STRING)
    private NotificationType type; // BOOKING, PAYMENT, MAINTENANCE, PROMOTION
    private boolean isRead; // UNREAD, READ
    private String userId; // Người dùng nhận thông báo
}

