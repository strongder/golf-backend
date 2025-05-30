package com.example.golf.service;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.services.request.CreateServiceRequest;
import com.example.golf.dtos.services.response.ServicesResponse;
import com.example.golf.model.Services;

public interface ServicesService extends BaseService<Services, String> {
    String softDelete(String id);

    ServicesResponse createServices(CreateServiceRequest request);

    ServicesResponse updateServices(String id, CreateServiceRequest request);

    BaseSearchResponse<ServicesResponse> search(BaseSearchRequest request);

}
