package com.example.golf.service;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.tee_time.response.TeeTimeRequest;
import com.example.golf.dtos.tee_time.response.TeeTimeResponse;
import com.example.golf.model.TeeTime;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TeeTimeService extends BaseService<TeeTime, String> {
    List<TeeTimeResponse> getAvailableTeeTimes(String golfCourseId, LocalDate date);

    List<TeeTimeResponse> getTeeTimeByDateAndGolfCourseId(String golfCourseId, LocalDate date, int page, int size);

    BaseSearchResponse<TeeTimeResponse> search(BaseSearchRequest request);

    TeeTimeResponse updateTeeTime(String id, TeeTimeRequest teeTimeRequest);

    String softDeleteTeeTime(String id);

    Object holdTeeTime(String teeTimeId, int holes);
}
