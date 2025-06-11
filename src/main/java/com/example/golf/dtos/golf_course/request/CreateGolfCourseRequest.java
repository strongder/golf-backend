package com.example.golf.dtos.golf_course.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreateGolfCourseRequest {
    private String code;
    private String name;
    private String location;
    private MultipartFile image;
    private float length; // do dai san golf
    private String description;
    private int duration; // thoi gian choi
    private int holes;
}
