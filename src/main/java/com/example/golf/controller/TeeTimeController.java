package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.tee_time.response.TeeTimeRequest;
import com.example.golf.dtos.tee_time.response.TeeTimeResponse;
import com.example.golf.service.TeeTimeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/v1/tee-time")
public class TeeTimeController {

    @Autowired
    private TeeTimeService teeTimeService;

    @GetMapping("/available")
    public ApiResponse getAvailableTeeTimes(@RequestParam String golfCourseId,
                                            @RequestParam LocalDate date) {
        return ApiResponse.success(teeTimeService.getAvailableTeeTimes(golfCourseId, date));
    }

    @GetMapping("/by-date-and-golf-course")
    public ApiResponse getTeeTimeByDateAndGolfCourseId(@RequestParam(value = "golfCourseId", required = false) String golfCourseId,
                                                       @RequestParam(value = "date", required = false) LocalDate date,
                                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        return ApiResponse.success(teeTimeService.getTeeTimeByDateAndGolfCourseId(golfCourseId, date, page, size));
    }


    @PostMapping("/create")
    public ApiResponse createTeeTime(@RequestBody TeeTimeRequest request) {
        return ApiResponse.success(teeTimeService.save(request, TeeTimeResponse.class));
    }

    @PutMapping("/update/{id}")
    public ApiResponse updateTeeTime(@PathVariable String id, @RequestBody TeeTimeRequest request) {
        return ApiResponse.success(teeTimeService.updateTeeTime(id, request));
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse softDeleteTeeTime(@PathVariable String id) {
        return ApiResponse.success(teeTimeService.softDeleteTeeTime(id));
    }

    @Operation(summary = "hold tee time")
    @GetMapping("/hold")
    public ApiResponse holdTeeTime(@RequestParam String teeTimeId, @RequestParam int holes) {
        return ApiResponse.success(teeTimeService.holdTeeTime(teeTimeId, holes));
    }

    @Operation(summary = "search tee time")
    @PostMapping("/search")
    public ApiResponse searchTeeTime(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(teeTimeService.search(request));
    }
}
