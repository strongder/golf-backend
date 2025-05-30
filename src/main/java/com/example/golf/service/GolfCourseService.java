package com.example.golf.service;

import com.example.golf.dtos.golf_course.request.CreateGolfCourseRequest;
import com.example.golf.dtos.golf_course.response.GolfCourseResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.model.GolfCourse;

public interface GolfCourseService extends BaseService<GolfCourse, String> {

    BaseSearchResponse<GolfCourseResponse> search(BaseSearchRequest request);

    Object create(CreateGolfCourseRequest request);

    Object update(String id, CreateGolfCourseRequest request);
}
