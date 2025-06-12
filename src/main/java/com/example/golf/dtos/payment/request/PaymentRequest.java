package com.example.golf.dtos.payment.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String id;
    private String userId; // id of user
    private String device;
    private String referenceId; // id of booking or membership
    private double amount; // amount of money
    private String type; // BOOKING, MEMBERSHIP
    private String status; // PENDING, COMPLETED, FAILED
    private String paymentMethod; // "cash", "card", "VNPay"
}
