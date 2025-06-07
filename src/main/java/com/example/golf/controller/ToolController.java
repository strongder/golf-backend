package com.example.golf.controller;

import com.example.golf.dtos.ApiResponse;
import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.tool.request.CreateToolRequest;
import com.example.golf.dtos.tool.response.ToolResponse;
import com.example.golf.service.ToolService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tool")
public class ToolController {

    @Autowired
    private ToolService toolsService;

    @Operation(summary = "Create tool")
    @PostMapping
    public ApiResponse createTool(@ModelAttribute CreateToolRequest request) {
        return ApiResponse.success(toolsService.createTool(request));
    }
    @Operation(summary = "update tool")
    @PutMapping("/upadate/{id}")
    public ApiResponse updateTool (@PathVariable("id") String id, @ModelAttribute CreateToolRequest request) {
        return ApiResponse.success(toolsService.update(id, request));
    }

    @Operation(summary = "Get tool by id")
    @GetMapping("/detail/{id}")
    public ApiResponse getToolById(@PathVariable("id") String id) {
        return ApiResponse.success(toolsService.getById(id));
    }

    @Operation(summary = "Delete tool by id")
    @DeleteMapping("/delete/{id}")
    public ApiResponse softDelete(@PathVariable("id") String id) {
        return ApiResponse.success(toolsService.softDelete(id));
    }

    @Operation(summary = "Get all tools")
    @GetMapping("/all")
    public ApiResponse getAll() {
        return ApiResponse.success(toolsService.findAll());
    }

    @Operation(summary = "Search tool")
    @PostMapping("/search")
    public ApiResponse search(@RequestBody BaseSearchRequest request) {
        return ApiResponse.success(toolsService.search(request));
    }

    @Operation(summary = "Get all tools by type golf_club")
    @GetMapping("/golf_club")
    public ApiResponse getAllGolfClub() {
        return ApiResponse.success(toolsService.getAllGolfClub());
    }

}
