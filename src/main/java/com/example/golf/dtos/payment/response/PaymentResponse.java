package com.example.golf.dtos.payment.response;

import com.example.golf.dtos.user.Response.DataFieldUser;
import com.example.golf.enums.PaymentStatus;
import com.example.golf.enums.PaymentType;
import lombok.Data;


import java.time.LocalDate;

@Data
public class PaymentResponse {
    private String id;
    private Double amount; // Số tiền thanh toán
    private String code;
    private String userId; // ID của người dùng
    private DataFieldUser user;
    private String transactionId; // ID giao dịch VNPay
    private String paymentMethod; // "cash", "card", "VNPay"
    private PaymentStatus status; // PENDING, COMPLETED, FAILED
    private PaymentType type; // BOOKING, MEMBERSHIP, SERVICE
    private String referenceId;
    private LocalDate createdAt;
}
