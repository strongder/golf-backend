package com.example.golf.service.impl;

import com.example.golf.config.payment.VnpayConfig;
import com.example.golf.dtos.payment.request.PaymentRequest;
import com.example.golf.dtos.payment.request.PaymentSearchRequest;
import com.example.golf.dtos.payment.response.PaymentResponse;
import com.example.golf.dtos.payment.response.VnpayResponse;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.user.Response.DataFieldUser;
import com.example.golf.enums.*;
import com.example.golf.exception.AppException;
import com.example.golf.model.Booking;
import com.example.golf.model.Membership;
import com.example.golf.model.Notification;
import com.example.golf.model.Payment;
import com.example.golf.repository.*;
import com.example.golf.utils.VnpayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl {

    @Autowired
    VnpayConfig vnpayConfig;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    @Lazy
    private BookingServiceImpl bookingServiceImpl;
    @Autowired
    private MembershipServiceImpl membershipServiceImpl;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentRepositoryImpl paymentRepositoryImpl;
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;
    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public VnpayResponse createVnPayPayment(PaymentRequest paymentRequest, HttpServletRequest request) {
        long amount = (long) (paymentRequest.getAmount() * 100); // Chuyển đổi từ Đơn vị tiền tệ
        String bankCode = request.getParameter("bankCode");
        // Tạo và lưu giao dịch Payment vào CSDL
        Payment payment = createPayment(paymentRequest);

        // Lưu transactionId cho giao dịch Payment
        String transactionId = VnpayUtil.getRandomNumber(8); // Tạo một transactionId ngẫu nhiên
        payment.setTransactionId(transactionId);
        paymentRepository.save(payment);
        // Tạo Map các tham số cho VNPay
        Map<String, String> vnpParamsMap = vnpayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_OrderInfo", paymentRequest.getReferenceId() + "**" + paymentRequest.getType());
        vnpParamsMap.put("vnp_TxnRef", transactionId); // Mã giao dịch
        vnpParamsMap.put("vnp_IpAddr", VnpayUtil.getIpAddress(request));

        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        // Tạo URL thanh toán
        String queryUrl = VnpayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VnpayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VnpayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;

        // Trả về VnpayResponse với URL thanh toán
        return VnpayResponse.builder()
                .code("00") // Thành công
                .message("Tạo thanh toán thành công")
                .paymentUrl(paymentUrl)
                .build();
    }

    @Transactional
    public VnpayResponse handlePaymentCallback(String status, String transactionId) {
        if (status == null || transactionId == null) {
            throw new AppException(ErrorResponse.INVALID_REQUEST_PARAMETERS);
        }
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new AppException(ErrorResponse.TRANSACTION_NOT_EXISTED));

        if (status.equals("00")) {
            payment.setStatus(PaymentStatus.COMPLETED);
            if (payment.getType() == PaymentType.BOOKING) {
                bookingServiceImpl.changeStatus(payment.getReferenceId(), BookingStatus.CONFIRMED);
                Notification notification = Notification.builder()
                        .userId(payment.getUserId())
                        .title("Payment Successful")
                        .type(NotificationType.PAYMENT)
                        .content("Your booking payment has been successfully completed.")
                        .dataId(payment.getReferenceId())
                        .isRead(false)
                        .build();
                // Lưu thông báo vào CSDL
                notificationRepository.save(notification);
                notificationServiceImpl.sendNotification(notification);
            } else if (payment.getType() == PaymentType.MEMBERSHIP) {
                membershipServiceImpl.changeStatus(payment.getReferenceId(), MembershipStatus.PAID);
                Notification notification = Notification.builder()
                        .userId(payment.getUserId())
                        .title("Payment Successful")
                        .type(NotificationType.PAYMENT)
                        .content("Your membership payment has been successfully completed.")
                        .dataId(payment.getReferenceId())
                        .isRead(false)
                        .build();
                // Lưu thông báo vào CSDL
                notificationRepository.save(notification);
                notificationServiceImpl.sendNotification(notification);
            }
            return VnpayResponse.builder()
                    .code("00")
                    .message("Payment successfully")
                    .build();
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            return VnpayResponse.builder()
                    .code("01")
                    .message("Payment failed")
                    .build();
        }
    }

    @Transactional
    public void updatePaymentStatusIfExpired(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));

        // Kiểm tra thời gian thanh toán
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = payment.getCreatedAt().plusMinutes(15); // Ví dụ: 15 phút

        if (now.isAfter(expirationTime) && payment.getStatus() == PaymentStatus.PENDING) {
            payment.setStatus(PaymentStatus.EXPIRED); // Cập nhật trạng thái thành "Paid"
            paymentRepository.save(payment);
        }
    }

    //
    @Scheduled(fixedRate = 180000)
    @Transactional
    public void checkPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findByStatus(PaymentStatus.PENDING);
        for (Payment payment : pendingPayments) {
            updatePaymentStatusIfExpired(payment.getId());
        }
    }

    @Transactional(readOnly = true)
    public BaseSearchResponse<PaymentResponse> searchPayments(PaymentSearchRequest request) {
       return paymentRepositoryImpl.searchPayment(request);
    }

    @Transactional
    public Payment createPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setUserId(paymentRequest.getUserId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setStatus(PaymentStatus.valueOf(paymentRequest.getStatus()));
        payment.setReferenceId(paymentRequest.getReferenceId());
        payment.setType(PaymentType.valueOf(paymentRequest.getType()));
        return paymentRepository.save(payment);
    }


    public PaymentResponse convertToResponse(Payment payment, Class<PaymentResponse> responseClass) {
        PaymentResponse paymentResponse =  modelMapper.map(payment, PaymentResponse.class);
        paymentResponse.setUser(modelMapper.map(userRepository.findById(payment.getUserId()), DataFieldUser.class));
        return responseClass.cast(paymentResponse);
    }

}