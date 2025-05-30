package com.example.golf.dtos.guest.response;

import com.example.golf.enums.GuestType;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GuestResponse {
    private String id;
    private String fullName;
    private String phone;
    private String avatar;
    private String address;
    private String gender;
    private String email;
    private LocalDate birthDate;
    private boolean isDelete;
    private GuestType role;
    private String userId;
    private int totalBooking;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
