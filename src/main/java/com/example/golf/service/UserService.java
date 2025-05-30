package com.example.golf.service;

import com.example.golf.dtos.user.Request.CreateUserRequest;
import com.example.golf.dtos.user.Response.UserResponse;
import com.example.golf.model.User;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService  extends BaseService<User, String> {
    UserResponse createStaff(CreateUserRequest request);
    UserResponse createGolfer(CreateUserRequest request);

    UserResponse updateUser(String id, CreateUserRequest request);

    String softDelete(String id);
    UserResponse getByEmail(String email);
    User getCurrentUser();


    @Transactional
    Object changeAvatar(MultipartFile file) throws IOException;
}
