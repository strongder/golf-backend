package com.example.golf.model;

import com.example.golf.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private String dataId; // ID của dữ liệu liên quan (ví dụ: bookingId, paymentId, etc.)

}

