package com.example.golf.service.impl;

import com.example.golf.dtos.search.BaseSearchRequest;
import com.example.golf.dtos.search.BaseSearchResponse;
import com.example.golf.dtos.staff.Request.CreateStaffRequest;
import com.example.golf.dtos.staff.Response.StaffResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.exception.AppException;
import com.example.golf.model.Staff;
import com.example.golf.model.User;
import com.example.golf.repository.StaffRepository;
import com.example.golf.service.StaffService;
import com.example.golf.utils.SearchUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffServiceImpl extends BaseServiceImpl<Staff, String> implements StaffService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SearchUtils<Staff> searchUtil;

    private final StaffRepository staffRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;

    public StaffServiceImpl(StaffRepository staffRepository) {
        super(staffRepository);
        this.staffRepository = staffRepository;
    }

    public StaffResponse createStaff(CreateStaffRequest request) {
        Optional<Staff> existingStaff = staffRepository.findByPhone(request.getPhone());
        if(existingStaff.isPresent()) {
            throw new AppException(ErrorResponse.ENTITY_EXISTED);
        }
        Staff staff = convertToEntity(request);
        staff.setCode(generationCodeStaff());
        this.save(staff);
        return convertToResponse(staff, StaffResponse.class);
    }

    @Override
    public StaffResponse updateStaff(String id, CreateStaffRequest request) {
        Staff existingStaff = staffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED)
        );
        modelMapper.map(request, existingStaff);
        repository.save(existingStaff);
        return convertToResponse(existingStaff, StaffResponse.class);
    }


    public StaffResponse getByUser(String userId) {
        Staff staff = staffRepository.findByUserId(userId).orElseThrow(
                () -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED)
        );
        return convertToResponse(staff, StaffResponse.class);
    }

    public BaseSearchResponse<StaffResponse> search(BaseSearchRequest request) {
        return searchUtil.search(Staff.class, request, staff -> modelMapper.map(staff, StaffResponse.class));
    }

    @Override
    public String softDelete(String id) {
        Staff staff = staffRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED)
        );
        staff.setDeleted(true);
        staffRepository.save(staff);
        return id;
    }

    @Override
    protected <Req> Staff convertToEntity(Req request) {
        return modelMapper.map(request, Staff.class);
    }

    @Override
    protected <Res> Res convertToResponse(Staff entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    public String generationCodeStaff ()
    {
        StringBuilder code = new StringBuilder("STF");
        int count = staffRepository.countStaff();
        return code.append(String.format("%03d", count + 1)).toString();
    }
}
