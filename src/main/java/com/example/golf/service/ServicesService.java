package com.example.golf.service;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.services.request.CreateServiceRequest;
import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.enums.ServiceType;
import com.example.golf.model.Services;

import java.util.List;

public interface ServicesService extends BaseService<Services, String> {
    List<ServicesResponse> getServiceByType(ServiceType type);

    
    String softDelete(String id);

    ServicesResponse createServices(CreateServiceRequest request);

    ServicesResponse updateServices(String id, CreateServiceRequest request);

    BaseSearchResponse<ServicesResponse> search(BaseSearchRequest request);


    List<ServicesResponse> getServiceByTypeNot(ServiceType type);
}
