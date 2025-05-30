package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.payment.request.PaymentRequest;
import com.example.golf.dtos.payment.request.PaymentSearchRequest;
import com.example.golf.dtos.payment.response.VnpayResponse;
import com.example.golf.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentServiceImpl paymentService;


    @PostMapping("/create")
    public ApiResponse createPayment(@RequestBody PaymentRequest paymentRequest) {
        return ApiResponse.success(paymentService.createPayment(paymentRequest));
    }

    @PostMapping("/vn-pay")
    public ApiResponse createPayment(@RequestBody PaymentRequest paymentRequest, HttpServletRequest request) {
        return  ApiResponse.success(paymentService.createVnPayPayment(paymentRequest, request));
    }

    @GetMapping("/vnpay-callback")
    public ApiResponse payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String transactionId = request.getParameter("vnp_TxnRef");
        // Gọi phương thức từ service để xử lý callback
        VnpayResponse vnpayResponse = paymentService.handlePaymentCallback(status, transactionId);
        return ApiResponse.success(vnpayResponse);
    }

    @PostMapping("/search")
    public ApiResponse searchPayment(@RequestBody PaymentSearchRequest request) {
        return ApiResponse.success(paymentService.searchPayments(request));
    }
}
