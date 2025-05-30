package com.example.golf.service.impl;

import com.example.golf.model.Maintenance;
import com.example.golf.repository.MaintenanceRepository;
import com.example.golf.service.MaintenanceService;

import org.springframework.stereotype.Service;

@Service
public class MaintenanceServiceImpl extends BaseServiceImpl<Maintenance, String> implements MaintenanceService {

    MaintenanceRepository maintenanceRepository;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository) {
        super(maintenanceRepository);
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    protected <Req> Maintenance convertToEntity(Req request) {
        return null;
    }

    @Override
    protected <Res> Res convertToResponse(Maintenance entity, Class<Res> responseType) {
        return null;
    }
}
