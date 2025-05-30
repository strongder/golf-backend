package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.tee_time_config.request.TeeTimeConfigRequest;
import com.example.golf.dtos.tee_time_config.response.TeeTimeConfigResponse;
import com.example.golf.service.TeeTimeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tee-time-config")
public class TeeTimeConfigController {

    @Autowired
    private TeeTimeConfigService teeTimeConfigService;

    @PostMapping("/create")
    public ApiResponse createTeeTimeConfig(@RequestBody TeeTimeConfigRequest request) {
        return ApiResponse.success(teeTimeConfigService.createTeeTimeConfig(request));
    }

    @PutMapping("/{id}")
    public ApiResponse updateTeeTimeConfig(@PathVariable("id") String id, @RequestBody TeeTimeConfigRequest request) {
        return ApiResponse.success(teeTimeConfigService.updateTeeTimeConfig(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteTeeTimeConfig(@PathVariable("id") String id) {
        return ApiResponse.success(teeTimeConfigService.softDelete(id));
    }
    @GetMapping("/active")
    public ApiResponse getActiveTeeTimeConfig() {
        return ApiResponse.success(teeTimeConfigService.getTeeTimeConfigTrue());
    }

}
