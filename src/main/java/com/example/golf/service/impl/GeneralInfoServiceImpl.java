package com.example.golf.service.impl;

import com.example.golf.model.GeneralInfo;
import com.example.golf.repository.GeneralInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralInfoServiceImpl extends BaseServiceImpl<GeneralInfo, String> {

    @Autowired
    public ModelMapper modelMapper;
    public  GeneralInfoRepository generalInfoRepository;
    public GeneralInfoServiceImpl(GeneralInfoRepository generalInfoRepository) {
        super(generalInfoRepository);
        this.generalInfoRepository = generalInfoRepository;
    }

    public GeneralInfo updateGeneralInfo(String id, GeneralInfo generalInfo) {
        GeneralInfo existingInfo = generalInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("General Info not found with id: " + id));
        // Update the existing info with new values
        modelMapper.map(generalInfo, existingInfo);
        return generalInfoRepository.save(generalInfo);
    }

    public GeneralInfo getGeneralInfo() {
        return generalInfoRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No General Info found"));
    }

    @Override
    protected <Req> GeneralInfo convertToEntity(Req request) {
        return modelMapper.map(request, GeneralInfo.class);
    }

    @Override
    protected <Res> Res convertToResponse(GeneralInfo entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }
}
