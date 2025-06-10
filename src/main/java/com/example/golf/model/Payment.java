package com.example.golf.model;

import com.example.golf.enums.PaymentStatus;
import com.example.golf.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "payment")
public class  Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double amount; // Số tiền thanh toán
    private String userId; // ID của người dùng
    private String transactionId; // ID giao dịch VNPay
    private String paymentMethod; // "cash", "card", "VNPay"
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, FAILED
    @Enumerated(EnumType.STRING)
    private PaymentType type; // BOOKING, MEMBERSHIP
    private String referenceId; // ID của booking/membership
}
