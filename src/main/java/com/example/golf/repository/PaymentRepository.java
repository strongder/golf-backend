package com.example.golf.repository;

import com.example.golf.enums.PaymentStatus;
import com.example.golf.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStatus(PaymentStatus paymentStatus);

    Optional<Payment> findByTransactionId(String transactionId);
}
