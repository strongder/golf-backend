package com.example.golf.model;

import com.example.golf.enums.BookingItemType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking_details")
public class BookingDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String bookingId;
    private String serviceId;
    private String toolId;
    private int quantity;
    private double unitPrice; // Giá tiền đơn vị
    private double totalPrice; // Giá tiền tổng cộng
}
