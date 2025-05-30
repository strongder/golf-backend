package com.example.golf.service;

import com.example.golf.dtos.tee_time_config.request.TeeTimeConfigRequest;
import com.example.golf.dtos.tee_time_config.response.TeeTimeConfigResponse;
import com.example.golf.model.TeeTimeConfig;

import java.util.List;

public interface TeeTimeConfigService extends BaseService<TeeTimeConfig, String> {
    TeeTimeConfigResponse createTeeTimeConfig(TeeTimeConfigRequest request);
    TeeTimeConfigResponse updateTeeTimeConfig(String id, TeeTimeConfigRequest request);
    List<TeeTimeConfigResponse> getTeeTimeConfigTrue();
    String softDelete(String id);
}
