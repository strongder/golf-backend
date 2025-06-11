package com.example.golf.dtos.staff.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateStaffRequest {
    private String id;
    private String email;
    private String phone;
    private String dob;
    private String gender;
    private String userId;
    private String fullName;
    private String address;
}
