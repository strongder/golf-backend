package com.example.golf.dtos.guest.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateGuestRequest {
    private String fullName;
    private String phone;
    private LocalDate birthDate; // Format: YYYY-MM-DD
    private String gender;
    private String address;
    private String email;
}
