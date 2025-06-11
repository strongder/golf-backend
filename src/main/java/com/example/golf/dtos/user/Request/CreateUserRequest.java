package com.example.golf.dtos.user.Request;

import com.example.golf.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CreateUserRequest {
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
}
