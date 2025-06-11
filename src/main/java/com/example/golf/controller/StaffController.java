package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.staff.Request.CreateStaffRequest;
import com.example.golf.dtos.staff.Response.StaffResponse;
import com.example.golf.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping
    @Operation(summary = "Create staff")
    public ApiResponse createStaff(@RequestBody CreateStaffRequest request) {
        return ApiResponse.success(staffService.createStaff(request));
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update staff")
    public ApiResponse updateStaff(@PathVariable("id") String id, @RequestBody CreateStaffRequest request) {
        return ApiResponse.success(staffService.updateStaff(id, request));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete staff")
    public ApiResponse deleteStaff(@PathVariable("id") String id) {
        return ApiResponse.success(staffService.softDelete(id));
    }

    @Operation(summary = "Get staff by id")
    @GetMapping("/{id}")
    public ApiResponse getStaffById(@PathVariable("id") String id) {
        return ApiResponse.success(staffService.getById(id, StaffResponse.class));
    }

    @Operation(summary = "Get all staff")
    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(staffService.search(request));
    }

    @GetMapping("/get-by-user/{userId}")
    @Operation(summary = "Get staff by user id")
    public ApiResponse getStaffByUserId(@PathVariable String userId) {
        return ApiResponse.success(staffService.getByUser(userId));
    }
}
