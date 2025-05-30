package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.membership_type.request.MembershipTypeRequest;
import com.example.golf.dtos.membership_type.response.MembershipTypeResponse;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.service.MembershipTypeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/membership-type")
public class MembershipTypeController {

    @Autowired
    private MembershipTypeService memberShipTypeService;


    @PostMapping("/create")
    public ApiResponse create(@RequestBody MembershipTypeRequest request) {
        return ApiResponse.success(memberShipTypeService.create(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse update(@PathVariable String id,  @RequestBody MembershipTypeRequest request) {
        return ApiResponse.success(memberShipTypeService.update(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse delete( @PathVariable String id) {
        return ApiResponse.success(memberShipTypeService.softDelete(id));
    }

    @GetMapping("/{id}")
    public ApiResponse getById(@PathVariable String id) {
        return ApiResponse.success(memberShipTypeService.getById(id, MembershipTypeResponse.class));
    }

    @GetMapping("/all")
    public ApiResponse getAll() {
        return ApiResponse.success(memberShipTypeService.findAllDeleteFalse());
    }


}
