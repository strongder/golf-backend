package com.example.golf.service.impl;

import com.example.golf.dtos.membership_type.request.MembershipTypeRequest;
import com.example.golf.dtos.membership_type.response.MembershipTypeResponse;
import com.example.golf.enums.ErrorResponse;
import com.example.golf.enums.MembershipStatus;
import com.example.golf.exception.AppException;
import com.example.golf.model.MembershipType;
import com.example.golf.repository.MemberShipTypeRepository;
import com.example.golf.service.MembershipTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MembershipTypeServiceImpl extends BaseServiceImpl<MembershipType, String> implements MembershipTypeService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    MemberShipTypeRepository memberShipTypeRepository;
    public MembershipTypeServiceImpl(MemberShipTypeRepository repository) {
        super(repository);
        this.memberShipTypeRepository = repository;
    }

    @Override
    protected <Req> MembershipType convertToEntity(Req request) {
        return modelMapper.map(request, MembershipType.class);
    }
    @Override
    protected <Res> Res convertToResponse(MembershipType entity, Class<Res> responseType) {
        return modelMapper.map(entity, responseType);
    }

    @Transactional
    @Override
    public MembershipTypeResponse create(MembershipTypeRequest request) {
        MembershipType exist = memberShipTypeRepository.
                findByDeletedIsFalse(request.getName());
        if (exist != null) {
            throw new AppException(ErrorResponse.ENTITY_EXISTED);
        }
        MembershipType memberShipType = convertToEntity(request);
        memberShipType.setDeleted(false);
        memberShipTypeRepository.save(memberShipType);
        return convertToResponse(memberShipType, MembershipTypeResponse.class);
    }

    @Transactional
    @Override
    public MembershipTypeResponse update(String id, MembershipTypeRequest request) {
        MembershipType memberShipType = memberShipTypeRepository.
                findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        modelMapper.map(request, memberShipType);
        memberShipType.setId(id);
        memberShipTypeRepository.save(memberShipType);
        return convertToResponse(memberShipType, MembershipTypeResponse.class);
    }

    @Override
    public String softDelete(String id) {
        MembershipType memberShipType = memberShipTypeRepository.
                findById(id).orElseThrow(() -> new AppException(ErrorResponse.ENTITY_NOT_EXISTED));
        memberShipType.setDeleted(true);
        memberShipTypeRepository.save(memberShipType);
        return id;
    }

    @Override
    public List<MembershipTypeResponse> findAllDeleteFalse() {
        List<MembershipType> memberShipTypes = memberShipTypeRepository.findAllByDeletedFalse();
        return memberShipTypes.stream()
                .map(memberShipType -> convertToResponse(memberShipType, MembershipTypeResponse.class))
                .toList();
    }


}
