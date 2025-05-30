package com.example.golf.service;

import com.example.golf.dtos.membership_type.request.MembershipTypeRequest;
import com.example.golf.dtos.membership_type.response.MembershipTypeResponse;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.model.MembershipType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MembershipTypeService extends BaseService<MembershipType, String> {

    @Transactional
    MembershipTypeResponse create(MembershipTypeRequest request);

    @Transactional
    MembershipTypeResponse update(String id, MembershipTypeRequest request);

    String softDelete(String id);

    List<MembershipTypeResponse> findAllDeleteFalse();


}
