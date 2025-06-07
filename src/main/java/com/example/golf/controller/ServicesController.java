package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.booking.Request.CreateBookingRequest;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.services.request.CreateServiceRequest;
import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.dtos.tool.request.CreateToolRequest;
import com.example.golf.service.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    @Operation(summary = "Create services")
    @PostMapping
    public ApiResponse createServices(@ModelAttribute CreateServiceRequest request) {
        return ApiResponse.success(servicesService.createServices(request));
    }

    @Operation(summary = "Get all services")
    @GetMapping("/all")
    public ApiResponse getAllServices() {
        return ApiResponse.success(servicesService.findAll(ServicesResponse.class));
    }

    @Operation(summary = "Create list ")
    @PostMapping("/save-all")
    private ApiResponse saveAll(@RequestBody List<CreateServiceRequest> request) {
        return ApiResponse.success(servicesService.saveAll(request));
    }

    @Operation(summary = "update services")
    @PutMapping("/{id}")
    public ApiResponse updateServices (@PathVariable("id") String id, @ModelAttribute CreateServiceRequest request) {
        return ApiResponse.success(servicesService.updateServices(id, request));
    }

    @Operation(summary = "Get services by id")
    @GetMapping("/{id}")
    public ApiResponse getServicesById(@PathVariable("id") String id) {
        return ApiResponse.success(servicesService.getById(id, ServicesResponse.class));
    }

    @Operation(summary = "Delete services by id")
    @DeleteMapping("/{id}")
    public ApiResponse softDelete(@PathVariable("id") String id) {
        return ApiResponse.success(servicesService.softDelete(id));
    }

    @Operation(summary = "Search services")
    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(servicesService.search(request));
    }
}
