package com.example.golf.service;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.tool.request.CreateToolRequest;
import com.example.golf.model.Tool;
import com.example.golf.dtos.tool.response.ToolResponse;

import java.util.List;

public interface ToolService {

    String softDelete(String id);
    ToolResponse createTool(CreateToolRequest request);
    ToolResponse update(String id, CreateToolRequest request);
    void calcQuantity(String itemId, int quantity, boolean isAdd);
    BaseSearchResponse<ToolResponse> search(BaseSearchRequest request);
    ToolResponse getById(String id);

    List<ToolResponse> findAll();

    List<ToolResponse> getToolByType(String type);
}
