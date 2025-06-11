package com.example.golf.service.impl;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.tool.request.CreateToolRequest;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.enums.ToolStatus;
import com.example.golf.exception.AppException;
import com.example.golf.model.Services;
import com.example.golf.model.Tool;
import com.example.golf.dtos.tool.response.ToolResponse;
import com.example.golf.repository.ToolsRepository;
import com.example.golf.service.ToolService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SearchUtils<Tool> searchUtil;
    @Autowired
    public ToolsRepository toolsRepository;
    @Autowired
    private FileService fileService;

    public ToolResponse createTool(CreateToolRequest request) {
        Tool tool = convertToEntity(request, Tool.class);
        tool.setStatus(ToolStatus.AVAILABLE);
        tool.setCode(generateCode());
        Tool savedTool = toolsRepository.save(tool);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), savedTool);
        }
        return convertToResponse(tool, ToolResponse.class);
    }

    public ToolResponse update(String id, CreateToolRequest request) {
        Tool tool = toolsRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(request, tool);
        if (request.getImage() != null) {
            uploadFileAsync(request.getImage(), tool);
        }
        toolsRepository.save(tool);
        return convertToResponse(tool, ToolResponse.class);
    }

    public void calcQuantity(String id, int quantity, boolean isAdd) {
        Tool tools = toolsRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        if (isAdd) {
            tools.setQuantity(tools.getQuantity() - quantity);
        } else {
            tools.setQuantity(tools.getQuantity() + quantity);
        }
        toolsRepository.save(tools);
    }

    public ToolResponse getById(String id) {
        Tool tool = toolsRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        return convertToResponse(tool, ToolResponse.class);
    }

    public List<ToolResponse> findAll() {
        List<Tool> tools = toolsRepository.findByToolDeleteFalse();
        return modelMapper.map(tools, new TypeToken<List<ToolResponse>>() {}.getType());
    }

    @Override
    public List<ToolResponse> getToolByType(String type) {
        List<Tool> tools = toolsRepository.findToolByType(type);
        return modelMapper.map(tools, new TypeToken<List<ToolResponse>>() {}.getType());
    }


    @Override
    public BaseSearchResponse<ToolResponse> search(BaseSearchRequest request) {
        return searchUtil.search(Tool.class, request, tools -> convertToResponse(tools, ToolResponse.class));
    }

    public String softDelete(String id) {
        Tool tools = toolsRepository.findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        tools.setDeleted(true);
        toolsRepository.save(tools);
        return id;
    }

    protected <Req> Tool convertToEntity(Req request, Class<Tool> entityType) {
        return modelMapper.map(request, entityType);
    }

    protected <Res> Res convertToResponse(Tool entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    public String generateCode() {
        return "TL" + (toolsRepository.count() + 1);
    }

    @Async
    public void uploadFileAsync(MultipartFile file, Tool tool) {
        try {
            String fileName = fileService.uploadFile(file); // upload file bình thường
            tool.setImageUrl(fileName);
            toolsRepository.save(tool);
        } catch (IOException e) {
            // log error
        }
    }
}
