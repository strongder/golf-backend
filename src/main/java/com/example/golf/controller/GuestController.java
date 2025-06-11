package com.example.golf.controller;

import com.example.golf.dtos.guest.request.UpdateGuestRequest;
import com.example.golf.dtos.guest.response.GuestResponse;
import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @GetMapping("/{id}")
    public ApiResponse getGuestById(@PathVariable String id) {
        return ApiResponse.success(guestService.getById(id, GuestResponse.class));
    }

    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(guestService.search(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse softDelete(@PathVariable String id) {
        return ApiResponse.success(guestService.softDelete(id));
    }
    @GetMapping("/get-by-user/{userId}")
    public ApiResponse getGuestByUserId(@PathVariable String userId) {
        return ApiResponse.success(guestService.getGuestByUserId(userId));
    }
    @PutMapping("/update/{id}")
    public ApiResponse updateGuest(@PathVariable String id, @RequestBody UpdateGuestRequest request) {
        return ApiResponse.success(guestService.updateGuest(id, request));
    }
}
