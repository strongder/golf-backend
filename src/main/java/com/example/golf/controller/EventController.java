package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.event.request.CreateEventRequest;
import com.example.golf.dtos.event.request.EventForUserRequest;
import com.example.golf.dtos.event.response.EventResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {

    @Autowired
    private  EventService eventService;

    @PostMapping("/create")
    public ApiResponse create(@ModelAttribute CreateEventRequest request){
        return ApiResponse.success(eventService.create(request));
    }

    @PutMapping("/update/{id}")
    public ApiResponse update(@PathVariable String id, @ModelAttribute CreateEventRequest request){
        return ApiResponse.success(eventService.update(id, request));
    }

    @GetMapping("/detail/{id}")
    public ApiResponse get(@PathVariable String id){
        return ApiResponse.success(eventService.getById(id, EventResponse.class));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse delete(@PathVariable String id){
        return ApiResponse.success(eventService.softDelete(id));
    }

    @PostMapping("/for-user")
    public ApiResponse getEventByTypeAndDate(@RequestBody EventForUserRequest eventForUserRequest){
        return ApiResponse.success(eventService.getEventByTypeAndDate(eventForUserRequest));
    }
    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request){
        return ApiResponse.success(eventService.search(request));
    }
    // lay ra khuyen mai cho user
    @GetMapping("/promotion-for-booking/{userId}")
    public ApiResponse getPromotionForUser(@PathVariable String userId) {
        return ApiResponse.success(eventService.getPromotionEventsForUser(userId));
    }
}
