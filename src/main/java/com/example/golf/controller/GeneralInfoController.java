package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.model.GeneralInfo;
import com.example.golf.service.impl.GeneralInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/general-info")
public class GeneralInfoController {

    @Autowired
    private GeneralInfoServiceImpl generalInfoService;

    @PostMapping("/create")
    public ApiResponse createGeneralInfo(@RequestBody GeneralInfo request) {
        return  ApiResponse.success(generalInfoService.save(request, GeneralInfo.class));
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateGeneralInfo(@PathVariable String id, @RequestBody GeneralInfo request) {
        GeneralInfo updatedInfo = generalInfoService.updateGeneralInfo(id, request);
        return ApiResponse.success(updatedInfo);
    }

    @GetMapping()
    public ApiResponse getGeneralInfoById() {
        GeneralInfo generalInfo = generalInfoService.getGeneralInfo();
        return ApiResponse.success(generalInfo);
    }
}
