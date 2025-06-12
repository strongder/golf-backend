package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.service.impl.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @Autowired
    public NotificationController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable String userId) {
        return notificationService.subscribe(userId);
    }

    @GetMapping("/user")
    public ApiResponse getNotificationByUser()
    {
        return ApiResponse.success(notificationService.getByUser());
    }

    @PutMapping("/mark-as-read/{notificationId}")
    public ApiResponse markAsRead(@PathVariable String notificationId) {
        return ApiResponse.success(notificationService.markAsRead(notificationId));
    }
    @PutMapping("/mark-all-as-read/{userId}")
    public ApiResponse markAllAsRead(@PathVariable String userId) {
        notificationService.markAllAsRead(userId);
        return ApiResponse.success("All notifications marked as read for user: " + userId);
    }

    @GetMapping("/unread-count")
    public ApiResponse getUnreadCount() {
        return ApiResponse.success(notificationService.getUnreadCount());
    }


    @DeleteMapping("/delete/{notificationId}")
    public ApiResponse deleteNotification(@PathVariable String notificationId) {
        return ApiResponse.success(notificationService.deleteNotification(notificationId));
    }
}
