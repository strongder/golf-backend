package com.example.golf.dtos.event.request;

import lombok.Data;

import java.time.LocalDate;


@Data
public class EventForUserRequest {
    private String status;
    private String type;
    private LocalDate date;
    private String role;
}
