package com.example.golf.service.impl;

import com.example.golf.dtos.golf_course.request.CreateGolfCourseRequest;
import com.example.golf.dtos.golf_course.response.GolfCourseResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.enums.GolfCourseStatus;
import com.example.golf.model.GolfCourse;
import com.example.golf.model.User;
import com.example.golf.repository.GolfCourseRepository;
import com.example.golf.service.GolfCourseService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GolfCourseServiceImpl extends BaseServiceImpl<GolfCourse, String> implements GolfCourseService {

    GolfCourseRepository golfCourseRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SearchUtils<GolfCourse> searchUtil;
    @Autowired
    private FileService fileService;

    public GolfCourseServiceImpl(GolfCourseRepository golfCourseRepository) {
        super(golfCourseRepository);
        this.golfCourseRepository = golfCourseRepository;

    }

    @Transactional(readOnly = true)
    public BaseSearchResponse<GolfCourseResponse> search(BaseSearchRequest request) {
        return searchUtil.search(GolfCourse.class, request, golfCourse -> convertToResponse(golfCourse, GolfCourseResponse.class));
    }

    @Override
    public GolfCourseResponse create(CreateGolfCourseRequest request) {
        GolfCourse golfCourse = convertToEntity(request);
        golfCourse.setStatus(GolfCourseStatus.ACTIVE);
        golfCourse.setCode(generateCode());
        golfCourse = golfCourseRepository.save(golfCourse);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), golfCourse);
        }
        return convertToResponse(golfCourse, GolfCourseResponse.class);
    }

    @Override
    public GolfCourseResponse update(String id, CreateGolfCourseRequest request) {
        GolfCourse golfCourse = golfCourseRepository.findById(id).orElseThrow(() -> new RuntimeException("Golf course not found"));
        modelMapper.map(request, golfCourse);
        golfCourse.setStatus(GolfCourseStatus.ACTIVE);
        golfCourse = golfCourseRepository.save(golfCourse);
        return convertToResponse(golfCourse, GolfCourseResponse.class);
    }

    @Override
    protected GolfCourse convertToEntity(Object request) {
        return modelMapper.map(request, GolfCourse.class);
    }

    @Override
    protected <Res> Res convertToResponse(GolfCourse entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    public String generateCode() {
        //dinh dang GC001
        return "GC" + String.format("%03d", golfCourseRepository.count() + 1);
    }

    @Async
    public void uploadFileAsync(MultipartFile file, GolfCourse golfCourse) {
        try {
            String fileName = fileService.uploadFile(file); // upload file bình thường
            golfCourse.setImage(fileName);
            golfCourseRepository.save(golfCourse);
        } catch (IOException e) {
            // log error
        }
    }
}
