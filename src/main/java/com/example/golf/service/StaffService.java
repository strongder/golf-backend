package com.example.golf.service;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.staff.Request.CreateStaffRequest;
import com.example.golf.dtos.staff.Response.StaffResponse;
import com.example.golf.model.Staff;

public interface StaffService extends BaseService<Staff, String> {
    StaffResponse createStaff(CreateStaffRequest request);

    StaffResponse updateStaff(String id, CreateStaffRequest request);

    BaseSearchResponse<StaffResponse> search(BaseSearchRequest request);
    String softDelete(String id);
    StaffResponse getByUser(String userId);
}
