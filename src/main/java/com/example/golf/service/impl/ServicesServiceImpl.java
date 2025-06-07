package com.example.golf.service.impl;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.services.request.CreateServiceRequest;
import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.GolfCourse;
import com.example.golf.model.Services;
import com.example.golf.repository.ServicesRepository;
import com.example.golf.service.ServicesService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ServicesServiceImpl extends BaseServiceImpl<Services, String> implements ServicesService {

    ServicesRepository servicesRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SearchUtils<Services> searchUtil;
    @Autowired
    private FileService fileService;

    public ServicesServiceImpl(ServicesRepository servicesRepository) {
        super(servicesRepository);
        this.servicesRepository = servicesRepository;

    }
    @Override
    public ServicesResponse createServices(CreateServiceRequest request) {
        Services services = convertToEntity(request);
        services.setStatus("ACTIVE");
        services.setCode(generateCode());
        services.setDeleted(false);
        Services savedService = servicesRepository.save(services);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), savedService);
        }
        return convertToResponse(savedService, ServicesResponse.class);
    }

    @Override
    public ServicesResponse updateServices(String id, CreateServiceRequest request) {
        Services services = servicesRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(request, services);
        services = servicesRepository.save(services);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), services);
        }
        return convertToResponse(services, ServicesResponse.class);
    }

    @Override
    public BaseSearchResponse<ServicesResponse> search(BaseSearchRequest request) {
        return searchUtil.search(Services.class, request, service -> convertToResponse(service, ServicesResponse.class));
    }

    public String softDelete(String id) {
        Services services = servicesRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        services.setDeleted(true);
        this.save(services);
        return id;
    }

    @Override
    protected Services convertToEntity(Object request) {
        return modelMapper.map(request, Services.class);
    }

    @Override
    protected <Res> Res convertToResponse(Services entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    public String generateCode() {
        return "SV" + servicesRepository.count();
    }


    @Async
    public void uploadFileAsync(MultipartFile file, Services services) {
        try {
            String fileName = fileService.uploadFile(file); // upload file bình thường
            services.setImageUrl(fileName);
            servicesRepository.save(services);
        } catch (IOException e) {
            // log error
        }
    }
}
