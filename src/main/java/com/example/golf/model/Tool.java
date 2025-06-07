package com.example.golf.model;

import com.example.golf.enums.ToolStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "tools")
public class Tool extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String code;
    private String imageUrl; // URL of the tool image
    private String name; // Tên dụng cụ (Gậy golf, Máy đo khoảng cách...)
    private int quantity;
    private String type; //loọi thiet bi GOLF_CLUB, GOLF_BALL, GOLF_BAG, GOLF_CART, GOLF_SHOES, GOLF_GLOVE, GOLF_TEE
    private double rentPrice; // Giá thuê
    private String provider; // Nhà cung cấp
    @Enumerated(EnumType.STRING)
    private ToolStatus status; // AVAILABLE, UNAVAILABLE
}
