package com.example.golf.controller;

import com.example.golf.dtos.booking.Request.CreateBookingRequest;
import com.example.golf.dtos.golf_course.request.CreateGolfCourseRequest;
import com.example.golf.dtos.golf_course.response.GolfCourseResponse;
import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.user.Response.UserResponse;
import com.example.golf.service.GolfCourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/golf-course")
public class GolfCourseController {

    @Autowired
    private GolfCourseService golfCourseService;

    @PostMapping
    public ApiResponse create(@ModelAttribute  CreateGolfCourseRequest request) {
        return ApiResponse.success(golfCourseService.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse update(@PathVariable("id") String id, @ModelAttribute CreateGolfCourseRequest request) {
        return ApiResponse.success(golfCourseService.update(id, request));
    }

    @GetMapping("/all")
    public ApiResponse getAll() {
        return ApiResponse.success(golfCourseService.findAll(GolfCourseResponse.class));
    }

    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable("id") String id) {
        return ApiResponse.success(golfCourseService.getById(id, GolfCourseResponse.class));
    }
}
