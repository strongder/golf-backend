package com.example.golf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth/otp")
public class OtpController {
//    @Autowired
//    private EmailService emailService;
//
//    private final Map<String, String> otpStorage = new HashMap<>();
//
//    @PostMapping("/send")
//    public ApiResponse<?> sendOtp(@RequestParam String email) {
//        if (otpStorage.containsKey(email)) {
//            otpStorage.remove(email);
//        }
//        String otp = emailService.generateOTP();
//        otpStorage.put(email, otp); // Lưu OTP tạm thời (hoặc lưu vào DB)
//        emailService.sendOTP(email, otp);
//        return ApiResponse.builder().code(200).message("OTP sent!").build();
//    }
//
//    @PostMapping("/verify")
//    public ApiResponse<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
//        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
//            otpStorage.remove(email);
//            return ApiResponse.builder().code(200).message("OTP verified!").build();
//        }
//        return ApiResponse.builder().code(1000).message("OTP not verified!").build();
//    }
}
