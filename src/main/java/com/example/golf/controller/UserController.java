package com.example.golf.controller;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.user.Request.ChangePasswordRequest;
import com.example.golf.dtos.user.Request.CreateUserRequest;
import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.user.Response.UserResponse;
import com.example.golf.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public ApiResponse getUserById(@PathVariable("id") String id) {
        return ApiResponse.success(userService.getById(id, UserResponse.class));
    }

    @Operation(summary = "Create account for staff")
    @PostMapping("/create-staff")
    public ApiResponse createStaff( @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createStaff(request));
    }


    @Operation(summary = "Update user")
    @PutMapping("/{id}")
    public ApiResponse updateUser(@PathVariable("id") String id, @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    public ApiResponse deleteUser(@PathVariable("id")String id) {
        return ApiResponse.success(userService.softDelete(id));
    }

    @Operation(summary = "Get all users")
    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(userService.search(request));
    }

    @Operation(summary = "Get user by email")
    @GetMapping("/email/{email}")
    public ApiResponse getUserByEmail(@PathVariable("email") String email) {
        return ApiResponse.success(userService.getByEmail(email));
    }

    // lay user hien tain
    @GetMapping("/me")
    public ApiResponse getCurrentUser() {
        return ApiResponse.success(userService.getCurrentUser());
    }

    @Operation(summary = "update avatar")
    @PostMapping("/avatar")
    public ApiResponse updateAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(userService.changeAvatar(file));
    }

    @PutMapping("/change-password")
    public ApiResponse changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ApiResponse.success("Password changed successfully");
        }
}
