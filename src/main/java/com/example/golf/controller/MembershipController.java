package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.membership.request.CreateMembershipRequest;
import com.example.golf.dtos.membership.request.MembershipSearchRequest;
import com.example.golf.dtos.membership.response.MembershipResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.service.MembershipService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

    @Autowired
    private MembershipService memberShipService;

    @GetMapping("/{id}")
    public ApiResponse getMembership(@PathVariable String id) {
        return ApiResponse.success(memberShipService.getById(id, MembershipResponse.class));
    }

    @PostMapping("/register")
    public ApiResponse registerMembership(@RequestBody CreateMembershipRequest request) {
        return ApiResponse.success(memberShipService.registerMembership(request));
    }

    @PostMapping("/lock/{membershipId}")
    public ApiResponse lockMemberShip(@PathVariable String membershipId) {
        return ApiResponse.success(memberShipService.inActiveMemberShip(membershipId));
    }

    @PostMapping("/confirm/{membershipId}")
    public ApiResponse confirmMembership(@PathVariable String membershipId) {
        return ApiResponse.success(memberShipService.confirmMembership(membershipId));
    }

    @PostMapping("/cancel/{membershipId}")
    public ApiResponse cancelMembership(@PathVariable String membershipId) {
        return ApiResponse.success(memberShipService.cancelMembership(membershipId));
    }

    @PostMapping("/renew/{membershipId}")
    public ApiResponse renewMembership(@PathVariable String membershipId) {
        return ApiResponse.success(memberShipService.renewMembership(membershipId));
    }

    @GetMapping("/history/{userId}")
    public ApiResponse getMembershipHistory(@PathVariable String userId) {
        return ApiResponse.success(memberShipService.getMembershipHistory(userId));
    }

    @PostMapping("/search")
    public ApiResponse searchMembership(@RequestBody MembershipSearchRequest request) {
        return ApiResponse.success(memberShipService.search(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse softDeleteMembership(@PathVariable String id) {
        return ApiResponse.success(memberShipService.softDelete(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Membership Type by User ID")
    public ApiResponse getByUserId(@PathVariable String userId) {
        return ApiResponse.success(memberShipService.getByUserId(userId));
    }

    @GetMapping("/user-status")
    @Operation(summary = "Get Membership Type by User ID and Status")
    public ApiResponse getByUserAndStatus(@RequestParam String userId, @RequestParam MembershipStatus status) {
        return ApiResponse.success(memberShipService.getByUserAndStatus(userId, status));
    }

}
