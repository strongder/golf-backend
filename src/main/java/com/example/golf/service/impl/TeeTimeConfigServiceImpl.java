package com.example.golf.service.impl;

import com.example.golf.dtos.golf_course.response.DataFieldGolfCourse;
import com.example.golf.dtos.tee_time_config.request.TeeTimeConfigRequest;
import com.example.golf.dtos.tee_time_config.response.TeeTimeConfigResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.TeeTimeConfig;
import com.example.golf.repository.TeeTimeConfigRepository;
import com.example.golf.service.GolfCourseService;
import com.example.golf.service.TeeTimeConfigService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
public class TeeTimeConfigServiceImpl extends BaseServiceImpl<TeeTimeConfig, String> implements TeeTimeConfigService {

    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public TeeTimeConfigRepository teeTimeConfigRepository;
    @Autowired
    private GolfCourseService golfCourseService;

    public TeeTimeConfigServiceImpl(TeeTimeConfigRepository teeTimeConfigRepository) {
        super(teeTimeConfigRepository);
        this.teeTimeConfigRepository = teeTimeConfigRepository;
    }

    @Transactional
    public TeeTimeConfigResponse createTeeTimeConfig(TeeTimeConfigRequest request) {
        TeeTimeConfig existingConfig = teeTimeConfigRepository.findTeeTimeConfigByGolfCourseIdAndDateType(request.getGolfCourseId(), request.getDateType());
        if (existingConfig != null) {
            throw new AppException(ErrorResponse.ENTITY_EXISTED);
        }
        TeeTimeConfig teeTimeConfig = convertToEntity(request);
        teeTimeConfig.setDeleted(false);
        teeTimeConfigRepository.save(teeTimeConfig);
        return convertToResponse(teeTimeConfig, TeeTimeConfigResponse.class);
    }

    public TeeTimeConfigResponse updateTeeTimeConfig(String id, TeeTimeConfigRequest request) {
        TeeTimeConfig teeTimeConfig = teeTimeConfigRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(request, teeTimeConfig);
        teeTimeConfig.setId(id);
        teeTimeConfigRepository.save(teeTimeConfig);
        return convertToResponse(teeTimeConfig, TeeTimeConfigResponse.class);
    }

    public List<TeeTimeConfigResponse> getTeeTimeConfigTrue() {
        List<TeeTimeConfig> teeTimeConfig = teeTimeConfigRepository.findByDeletedFalse();
        if (teeTimeConfig == null) {
            return Collections.singletonList((TeeTimeConfigResponse) List.of());
        }
        return modelMapper.map(teeTimeConfig, new TypeToken<List<TeeTimeConfigResponse>>() {
        }.getType());
    }

    @Override
    public String softDelete(String id) {
        TeeTimeConfig teeTimeConfig = teeTimeConfigRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        teeTimeConfig.setDeleted(true);
        teeTimeConfigRepository.save(teeTimeConfig);
        return id;
    }
    @Override
    protected <Req> TeeTimeConfig convertToEntity(Req request) {
        return modelMapper.map(request, TeeTimeConfig.class);
    }
    @Override
    protected <Res> Res convertToResponse(TeeTimeConfig entity, Class<Res> responseType) {
        TeeTimeConfigResponse response = modelMapper.map(entity, TeeTimeConfigResponse.class);
        response.setGolfCourse(modelMapper.map(golfCourseService.findById(entity.getGolfCourseId()), DataFieldGolfCourse.class));
        return (Res) response;
    }
}
