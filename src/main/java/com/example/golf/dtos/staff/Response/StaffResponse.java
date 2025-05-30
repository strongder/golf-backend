package com.example.golf.dtos.staff.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StaffResponse {
    private String id;
    private String code;
    private String email;
    private String avatar;
    private String phone;
    private String dob;
    private String gender;
    private String userId;
    private String fullName;
    private String address;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDelete;
    private String createdBy;
    private String updatedBy;
}
